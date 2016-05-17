package agents;

import alarmsOntology.*;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
 
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
            }
            if (args[0].equals("Calificar")) {
                ObtenerLosCalificados CalificadosBehaviour = new ObtenerLosCalificados(this);
                addBehaviour(CalificadosBehaviour);
            }
        }
        
        WaitPingAndReplyBehaviour PingBehaviour = new  WaitPingAndReplyBehaviour(this);
        MirarNuevosOAs tickerNuevosOAs = new MirarNuevosOAs(this, 10000);
        MirarNuevasAsesorias tickerNuevasAsesorias = new MirarNuevasAsesorias(this, 10000);
        
        //addBehaviour(tbf.wrap(tickerNuevosOAs));
        addBehaviour(tbf.wrap(tickerNuevasAsesorias));
        addBehaviour(PingBehaviour);
    }
    
    class MirarNuevosOAs extends TickerBehaviour {
        public MirarNuevosOAs(Agent a, long timer){
            super(a,timer);
        }
        
        @Override
        public void onTick(){
            //REQUEST
            String response = realizarRequest("learnings");
            
            response = response.substring(1, response.length() - 1);
            String[] aux_array = response.split(",");
            List oas = new ArrayList();
            
            for (String id : aux_array) {
                oas.add(Integer.parseInt(id));
            }
            
            if (oas.isEmpty()) {
                System.out.println("No hay nuevos objetos de aprendizaje!");
            } else {
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
            String response = realizarRequest("counselings");
            
            response = response.substring(1, response.length() - 1);
            String[] aux_array = response.split(",");
            List asesorias = new ArrayList();
            
            for (String id : aux_array) {
                asesorias.add(Integer.parseInt(id));
            }
            
            if (asesorias.isEmpty()) {
                System.out.println("No hay nuevas asesorias!");
            } else {
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
    
    // Clase que describe el comportamiento que permite recibir un mensaje
    // y contestarlo
    class WaitPingAndReplyBehaviour extends SimpleBehaviour {
        private boolean finished = false;
 
        public WaitPingAndReplyBehaviour(Agent a) {
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
                                // Recibido un INFORM con contenido correcto
                                EstudianteDenunciado ed = (EstudianteDenunciado) ce;
                                Estudiante e = ed.getEstudiante();
                                
                                System.out.println("Denuncia recibida");
                                System.out.println("Se ha hecho una denuncia de un estudiante"
                                        + " con cédula: " + e.getCedula()
                                        + " llamado " + e.getNombre() + " " + e.getApellido()
                                        + " con correo" + " " + e.getCorreo()
                                );
                                doDelete();
                                new Container().mainMenu();
                            }else if(ce instanceof EstudiantesCalificados){
                                ACLMessage reply = msg.createReply();
                                
                                BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
                                
                                System.out.println("Ingrese Entrega que va a calificar");
                                int entrega = Integer.parseInt(buff.readLine());

                                System.out.println("Ingrese Nota");
                                float nota = Float.parseFloat(buff.readLine());

                                ObtenerEntregaCalificada oec = new ObtenerEntregaCalificada();
                                oec.setId_entrega(entrega);
                                oec.setNota(nota);

                                getContentManager().fillContent(reply, oec);
                                send(reply);

                                System.out.println("\nHemos registrado su calificación");
                                
                                EstudiantesCalificados ec = (EstudiantesCalificados) ce;
                                
                                for (int i = 0; i < ec.getEstudiantes().size(); i++) {
                                    Estudiante e = (Estudiante)ec.getEstudiantes().get(i);
                                    System.out.println("Hemos registrado una nueva calificación a nombre"
                                            + " de " + e.getNombre() + " " + e.getApellido()
                                            + " en la entrega " + entrega
                                            + " y obtuvo una nota de " + nota
                                    );
                                }
                            } else if (ce instanceof EntregaCalificada) {
                                EntregaCalificada ec = (EntregaCalificada) ce;
                                Entrega e = ec.getEntrega();
                                
                                System.out.println("Entrega recibida");
                                System.out.println("La entrega recibida tiene los siguientes datos:\n"
                                        + "ID: " + e.getId()
                                        + "\nFecha: " + e.getFecha()
                                        + "\nEnunciado: " + e.getEnunciado()
                                );
                                
                                doDelete();
                                new Container().mainMenu();
                            } else if (ce instanceof OAsRecomendados) {
                                OAsRecomendados oasr = (OAsRecomendados) ce;
                                List oas = oasr.getOas();
                        
                                System.out.println("*********** Se han recomendado los siguientes Objetos de Aprendizaje a estos estudiantes:");
                        
                                for (int i = 0; i < oas.size(); i++) {
                                    ObjetoDeAprendizaje oa = (ObjetoDeAprendizaje) oas.get(i);
                                    List estudiantes = oa.getEstudiantes();

                                    System.out.println("*****************************************************************************************");
                                    System.out.println("Objeto de Aprendizaje #" + oa.getId());
                                    System.out.println("    Nombre: " + oa.getNombre());

                                    for (int j = 0; j < estudiantes.size(); j++) {
                                        Estudiante estudiante = (Estudiante) estudiantes.get(j);

                                        System.out.println("    Estudiante con cedula " + estudiante.getCedula());
                                        System.out.println("        Nombre: " + estudiante.getNombre() + " " + estudiante.getApellido());
                                        System.out.println("        Correo: " + estudiante.getCorreo());
                                    }
                                }
                            } else if (ce instanceof AsesoriasProgramadas) {
                                AsesoriasProgramadas ap = (AsesoriasProgramadas) ce;
                                List asesorias = ap.getAsesorias();
                        
                                System.out.println("*********** Se han programado las siguientes asesorias para estos estudiantes:");
                        
                                for (int i = 0; i < asesorias.size(); i++) {
                                    Asesoria asesoria = (Asesoria) asesorias.get(i);
                                    List estudiantes = asesoria.getEstudiantes();

                                    System.out.println("*****************************************************************************************");
                                    System.out.println("Asesoria #" + asesoria.getId());
                                    System.out.println("    Asesor: " + asesoria.getAsesor());
                                    System.out.println("    Fecha: " + asesoria.getFecha());
                                    System.out.println("    Salon: " + asesoria.getSalon());

                                    for (int j = 0; j < estudiantes.size(); j++) {
                                        Estudiante estudiante = (Estudiante) estudiantes.get(j);

                                        System.out.println("    Estudiante con cedula " + estudiante.getCedula());
                                        System.out.println("        Nombre: " + estudiante.getNombre() + " " + estudiante.getApellido());
                                        System.out.println("        Correo: " + estudiante.getCorreo());
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
    class ObtenerLosCalificados extends SimpleBehaviour{
        public ObtenerLosCalificados(Agent a){
            super(a);
        }
        @Override
        public boolean done(){
            return true;
        }
        @Override
        public void action(){
            AID r = new AID();
            r.setLocalName("Estudiante");
            
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setSender(getAID());
            msg.addReceiver(r);
            msg.setLanguage(codec.getName());
            msg.setOntology(ontologia.getName());
            try {
                System.out.println("Ingrese cédula de estudiantes que quiere calificar (separados por coma)");
                BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));           
                String[] grupo_de_estudiantes = buff.readLine().toString().split(",");

                ObtenerEstudiantesCalificados oec = new ObtenerEstudiantesCalificados();
                for (String id_estudiante : grupo_de_estudiantes) {
                    oec.addId_estudiantes(id_estudiante);
                }
                
                getContentManager().fillContent(msg, oec);
                send(msg);
            }catch (IOException ex) {
                Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Codec.CodecException ex) {
                Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OntologyException ex) {
                Logger.getLogger(Teacher.class.getName()).log(Level.SEVERE, null, ex);
            }
           
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
    
    private String realizarRequest(String modelo) {
        String url = "http://apimasalarms.herokuapp.com/" + modelo + "/index/differences";
        StringBuilder response = new StringBuilder();
        
        try {
            URL url_object = new URL(url);
            HttpURLConnection con = (HttpURLConnection) url_object.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = con.getResponseCode();
            //System.out.println("\nSending 'GET' request to URL : " + url);
            //System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
            );

            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return response.toString();
    }
}