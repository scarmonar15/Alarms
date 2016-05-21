package agents;

import alarmsOntology.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import jade.content.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import utils.SendEmail;
 
public class Teacher extends Agent {
 
    private final Codec codec = new SLCodec();
    private final Ontology ontologia = AlarmsOntology.getInstance();
    private final ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
    private final String USER_AGENT = "Mozilla/5.0";
    
    @Override
    protected void setup() {
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);
        Object[] args = getArguments();

        if (args != null) {
            if (args[0].equals("Denunciar")){
                ObtenerAlDenunciado SenderBehaviour = new ObtenerAlDenunciado(this);
                addBehaviour(SenderBehaviour);
            } else if (args[0].equals("Calificar")) {
                CalificarEntrega CalificarBehaviour = new CalificarEntrega(this);
                addBehaviour(CalificarBehaviour);
            } else if (args[0].equals("SimularFecha")) {
                DetectarIncumplimiento IncumplimientoBehaviour = new DetectarIncumplimiento(this, (String) args[1]);
                addBehaviour(IncumplimientoBehaviour);
            }
        }
        
        RecibirMensajes PingBehaviour = new  RecibirMensajes(this);
        MirarNuevosOAs tickerNuevosOAs = new MirarNuevosOAs(this, 10000);
        MirarNuevasAsesorias tickerNuevasAsesorias = new MirarNuevasAsesorias(this, 10000);
        
        //addBehaviour(tbf.wrap(tickerNuevosOAs));
        //addBehaviour(tbf.wrap(tickerNuevasAsesorias));
        addBehaviour(PingBehaviour);
    }
    
    class MirarNuevosOAs extends TickerBehaviour {
        public MirarNuevosOAs(Agent a, long timer){
            super(a,timer);
        }
        
        @Override
        public void onTick(){
            //REQUEST
            String response = realizarRequest("GET", "learnings", null);
            
            response = response.substring(1, response.length() - 1);
            String[] aux_array = response.split(",");
            List oas = new ArrayList();
            
            if (response.length() > 0) {
                for (String id : aux_array) {
                    oas.add(Integer.parseInt(id));
                }
            }
            
            if (!oas.isEmpty()){
                AID r = new AID();
                r.setLocalName("Estudiante");

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setSender(getAID());
                msg.addReceiver(r);
                msg.setLanguage(codec.getName());
                msg.setOntology(ontologia.getName());

                ObtenerObjetosDeAprendizaje oedp = new ObtenerObjetosDeAprendizaje();
                oedp.setId_OAs(oas);

                try {
                    getContentManager().fillContent(msg, oedp);
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                }

                send(msg);
            }
        }
    }
    
    class MirarNuevasAsesorias extends TickerBehaviour {
        public MirarNuevasAsesorias(Agent a, long timer){
            super(a,timer);
        }
        
        @Override
        public void onTick(){
            //REQUEST
            String response = realizarRequest("GET", "counselings", null);
            
            response = response.substring(1, response.length() - 1);
            String[] aux_array = response.split(",");
            List asesorias = new ArrayList();
            
            if (response.length() > 0) {
               for (String id : aux_array) {
                    asesorias.add(Integer.parseInt(id));
                } 
            }
            
            if (!asesorias.isEmpty()) {
                AID r = new AID();
                r.setLocalName("Estudiante");

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setSender(getAID());
                msg.addReceiver(r);
                msg.setLanguage(codec.getName());
                msg.setOntology(ontologia.getName());

                ObtenerAsesorias oa = new ObtenerAsesorias();
                oa.setId_asesorias(asesorias);

                try {
                    getContentManager().fillContent(msg, oa);
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                }

                send(msg);
            }
        }
    }
    
    class CalificarEntrega extends SimpleBehaviour {
        public CalificarEntrega(Agent a) {
            super(a);
        }
        
        @Override
        public boolean done() {
            return true;
        }
        
        @Override
        public void action() {
            AID r = new AID();
            r.setLocalName("Estudiante");
            
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setSender(getAID());
            msg.addReceiver(r);
            msg.setLanguage(codec.getName());
            msg.setOntology(ontologia.getName());
            
            try {
                BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
                
                System.out.print("Ingrese el ID de la entrega que quiere calificar: ");
                         
                int id_entrega = Integer.parseInt(buff.readLine());
                
                System.out.print("Ingrese los IDs de los equipos que quiere calificar (Separados por coma): ");
                
                String[] id_equipos = buff.readLine().split(",");

                ObtenerEntregaCalificada oec = new ObtenerEntregaCalificada();
                oec.setId_entrega(id_entrega);
                
                for (String id_equipo : id_equipos) {
                    oec.addId_equipos(Integer.parseInt(id_equipo));
                }
                
                getContentManager().fillContent(msg, oec);
                send(msg);
            } catch (IOException | Codec.CodecException | OntologyException ex) {
                Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    class DetectarIncumplimiento extends SimpleBehaviour {
        private boolean finished = false;
        private String fecha;
 
        public DetectarIncumplimiento(Agent a, String fecha) {
            super(a);
            this.fecha = fecha;
        }
        
        @Override
        public void action() {
            String[] opciones_request = new String[1];
            opciones_request[0] = this.fecha;
            
            String response = realizarRequest("GET", "assignments_date", opciones_request);

            JsonArray tareas = new JsonParser().parse(response).getAsJsonArray();
            
            if (tareas.size() > 0) {
                String mensaje, asunto;
            
                for (int i = 0; i < tareas.size(); i++) {
                    JsonObject tarea_object = tareas.get(i).getAsJsonObject();
                    Tarea tarea = new Tarea(tarea_object);
                    Entrega entrega = tarea.getEntrega();
                    Proyecto proyecto = entrega.getProyecto();
                    List estudiantes = tarea.getEstudiantes();
                    
                    asunto = "Denuncia por incumplimiento en la Tarea #" + entrega.getId();
                            
                    mensaje = "Se ha generado una Denuncia automatica para los siguientes Estudiantes por el incumplimiento de la Tarea #" + tarea.getId() + ": \n" +
                              "Tarea #" + tarea.getId() + "\n" +
                              "    Proyecto asociado: \n" + 
                              "        ID: " + proyecto.getId() + "\n" +
                              "        Titulo: " + proyecto.getTitulo() + "\n" +
                              "    Entrega asociada: \n" +
                              "        ID: " + entrega.getId() + "\n" +
                              "        Enunciado: " + entrega.getEnunciado() + "\n" +
                              "    Descripcion: " + tarea.getDescripcion() + "\n" +
                              "    Fecha limite: " + entrega.getFecha() + "\n" +
                              "    Lista de Estudiantes: \n";
                            
                    for (int j = 0; j < estudiantes.size(); j++) {
                        Estudiante estudiante = (Estudiante) estudiantes.get(j);
                        
                        mensaje += "        Estudiante con cedula " + estudiante.getCedula() + "\n" +
                                   "            Nombre: " + estudiante.getNombre() + " " + estudiante.getApellido() +
                                   "            Correo: " + estudiante.getCorreo();
                    }
                    
                    /*try {
                            SendEmail.generateAndSendEmail(asunto, profesor.getCorreo(), mensaje);
                    } catch (MessagingException ex) {
                            Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                    }*/
                }
            } else {
                System.out.println("No hay tareas para este día!");
            }
            
            doDelete();
            try {
                new Container().mainMenu();
            } catch (IOException ex) {
                Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        @Override
        public boolean done() {
            return finished;
        }
    }
    
    class RecibirMensajes extends SimpleBehaviour {
        private boolean finished = false;
 
        public RecibirMensajes(Agent a) {
            super(a);
        }
 
        @Override
        public void action() {
            MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchLanguage(codec.getName()),
                MessageTemplate.MatchOntology(ontologia.getName())
            );
        
            ACLMessage  msg = blockingReceive(mt);
            
            try {
                if(msg == null) {
                    System.out.println("No message received");
                } else {
                    if(msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD) {
                        System.out.println("Mensaje NOT UNDERSTOOD recibido");
                    } else {
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            ContentElement ce = getContentManager().extractContent(msg);
                            
                            if (ce instanceof EstudianteDenunciado) {
                                EstudianteDenunciado ed = (EstudianteDenunciado) ce;
                                Estudiante e = ed.getEstudiante();
                                
                                String asunto = "Estudiante Denunciado!";
                                
                                String mensaje = "Se ha hecho una denuncia de un estudiante"
                                        + " con cédula: " + e.getCedula()
                                        + " llamado " + e.getNombre() + " " + e.getApellido()
                                        + " con correo" + " " + e.getCorreo() + "\n";
                                
                                try {
                                    SendEmail.generateAndSendEmail(asunto, e.getCorreo(), mensaje);
                                } catch (MessagingException ex) {
                                    Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                                doDelete();
                                new Container().mainMenu();
                            } else if (ce instanceof EntregaCalificada) {
                                EntregaCalificada ec = (EntregaCalificada) ce;
                                Entrega entrega = ec.getEntrega();
                                Proyecto proyecto = entrega.getProyecto();
                                List equipos = entrega.getEquipos();
                                BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
                        
                                for (int i = 0; i < equipos.size(); i++) {
                                    Equipo equipo = (Equipo) equipos.get(i);
                                    List estudiantes = equipo.getEstudiantes();
                                    String asunto, mensaje;
                                    float nota;
                                    
                                    System.out.print("Ingrese la nota de la Entrega #" + entrega.getId() + " para el Equipo #" + equipo.getId() + ": ");
                                    nota = Float.parseFloat(buff.readLine());
                                    
                                    //TODO Realizar POST a teams_assignments con la nota
                                    String[] opciones_request = new String[2];
                                    opciones_request[0] = String.valueOf(entrega.getId());
                                    opciones_request[1] = "{'grade': '" + nota + "'}";
                                    
                                    realizarRequest("PUT", "assignments", opciones_request);
                                    
                                    asunto = "Entrega \"" + entrega.getEnunciado() + "\" calificada!";
                            
                                    mensaje = "Entrega #" + entrega.getId() + "\n" +
                                              "    ID Proyecto: " + proyecto.getId() + "\n" +
                                              "    Titulo del Proyecto: " + proyecto.getTitulo() + "\n" +
                                              "    Enunciado: " + entrega.getEnunciado() + "\n" +
                                              "    Fecha limite: " + entrega.getFecha() + "\n" +
                                              "    Nota: " + nota;
                                    
                                    for (int j = 0; j < estudiantes.size(); j++) {
                                        Estudiante estudiante = (Estudiante) estudiantes.get(j);
                                        
                                        /*try {
                                            SendEmail.generateAndSendEmail(asunto, estudiante.getCorreo(), mensaje);
                                        } catch (MessagingException ex) {
                                            Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
                                        }*/
                                    }
                                }
                                
                                doDelete();
                                new Container().mainMenu();
                            } else if (ce instanceof OAsRecomendados) {
                                OAsRecomendados oasr = (OAsRecomendados) ce;
                                List oas = oasr.getOas();
                        
                                for (int i = 0; i < oas.size(); i++) {
                                    ObjetoDeAprendizaje oa = (ObjetoDeAprendizaje) oas.get(i);
                                    List estudiantes = oa.getEstudiantes();
                                    String asunto, mensaje;
                                    
                                    asunto = "Objeto de Aprendizaje recomendado!";
                            
                                    mensaje = "Se le ha recomendado el siguiente Objeto de Aprendizaje: \n" +
                                              "Objeto de Aprendizaje #" + oa.getId() + "\n" +
                                              "    Nombre: " + oa.getNombre();

                                    for (int j = 0; j < estudiantes.size(); j++) {
                                        Estudiante estudiante = (Estudiante) estudiantes.get(j);
                                        
                                        try {
                                            SendEmail.generateAndSendEmail(asunto, estudiante.getCorreo(), mensaje);
                                        } catch (MessagingException ex) {
                                            Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                            } else if (ce instanceof AsesoriasProgramadas) {
                                AsesoriasProgramadas ap = (AsesoriasProgramadas) ce;
                                List asesorias = ap.getAsesorias();
                        
                                for (int i = 0; i < asesorias.size(); i++) {
                                    Asesoria asesoria = (Asesoria) asesorias.get(i);
                                    List estudiantes = asesoria.getEstudiantes();
                                    String asunto, mensaje;
                                    
                                    asunto = "Asesoria asignada!";
                            
                                    mensaje = "Asesoria #" + asesoria.getId() + "\n" +
                                              "    Asesor: " + asesoria.getAsesor() + "\n" +
                                              "    Fecha: " + asesoria.getFecha() + "\n" +
                                              "    Salon: " + asesoria.getSalon();

                                    for (int j = 0; j < estudiantes.size(); j++) {
                                        Estudiante estudiante = (Estudiante) estudiantes.get(j);
                                        
                                        try {
                                            SendEmail.generateAndSendEmail(asunto, estudiante.getCorreo(), mensaje);
                                        } catch (MessagingException ex) {
                                            Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                            } else {
                                // Recibido un INFORM con contenido incorrecto
                                ACLMessage reply = msg.createReply();

                                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                                reply.setContent("( UnexpectedContent (expected ping))");

                                send(reply);
                            }
                        } else {
                            // Recibida una performativa incorrecta
                            ACLMessage reply = msg.createReply();
                            
                            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                            reply.setContent("( (Unexpected-act "+ACLMessage.getPerformative(msg.getPerformative())+")( expected (inform)))");
                            
                            send(reply);
                        }
                    }
                }
            } catch (Codec.CodecException | OntologyException e) {
                System.out.println(e);
            } catch (IOException ex) {
                Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
 
        @Override
        public boolean done() {
            return finished;
        }
    }

    class ObtenerAlDenunciado extends SimpleBehaviour{
        private boolean finished = false;
        
        public ObtenerAlDenunciado(Agent a) {
            super(a);
        }

        @Override
        public void action() {
            try {
                AID r = new AID();
                r.setLocalName("Estudiante");
                
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setSender(getAID());
                msg.addReceiver(r);
                msg.setLanguage(codec.getName());
                msg.setOntology(ontologia.getName());
                
                System.out.print("Ingrese cédula del estudiante que quiere denunciar: ");
                BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
                String respuesta = buff.readLine();
                
                ObtenerEstudianteDenunciado oed = new ObtenerEstudianteDenunciado();
                oed.setId_estudiante(Integer.parseInt(respuesta));
                
                getContentManager().fillContent(msg, oed);
                send(msg);
                
                System.out.println("\nHemos registrado su denuncia");
            } catch (IOException | NumberFormatException | Codec.CodecException | OntologyException e) {
                System.out.println(e);
                finished = true;
            }
        }

        @Override
        public boolean done() {
            return true;
        }
    }
    
    private String realizarRequest(String method, String model, String[] args) {
        String url = "";
        
        if (method.equals("GET")) {
            url = "http://apimasalarms.herokuapp.com/" + model + "/index/differences";
        } else if (method.equals("PUT")) {
            url = "http://apimasalarms.herokuapp.com/" + model + "/" + args[0];
        }
        
        StringBuilder response = new StringBuilder();
        
        try {
            URL url_object = new URL(url);
            HttpURLConnection con = (HttpURLConnection) url_object.openConnection();

            con.setRequestMethod(method);
            con.setRequestProperty("User-Agent", USER_AGENT);

            if (method.equals("GET")) {
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
                );

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
            } else {
                con.setDoOutput(true);
                
                OutputStreamWriter out = new OutputStreamWriter(
                    con.getOutputStream()
                );
                
                out.write(args[1]);
                out.close();
                
                con.getInputStream();
            }  
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
    }
}