/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import utils.SendEmail;
 
public class Team extends Agent {
 
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
            if (args[0].equals("SimularFecha")) {
                PlazoEntregaCumplido pec = new PlazoEntregaCumplido(this, (String) args[1]);
                addBehaviour(pec);
            }
        }

        RecibirMensajes PingBehaviour = new RecibirMensajes(this);
        MirarNuevosProyectos tickerProyectos = new MirarNuevosProyectos(this, 5000);
        MirarNuevasEntregas tickerEntregas = new MirarNuevasEntregas(this, 5000);
        MirarNuevosEquipos tickerNuevosEquipos = new MirarNuevosEquipos(this, 5000);
        
        addBehaviour(tbf.wrap(tickerProyectos));
        addBehaviour(tbf.wrap(tickerEntregas));
        addBehaviour(tbf.wrap(tickerNuevosEquipos));
        addBehaviour(PingBehaviour);
    }
    
    class MirarNuevosProyectos extends TickerBehaviour {
        public MirarNuevosProyectos(Agent a, long timer){
            super(a,timer);
        }
        
        @Override
        public void onTick(){
            //REQUEST
            String response = realizarRequest("projects", null);
            
            response = response.substring(1, response.length() - 1);
            String[] aux_array = response.split(",");
            List proyectos = new ArrayList();
            
            if (response.length() > 0) {
                for (String id : aux_array) {
                    proyectos.add(Integer.parseInt(id));
                }
            }
            
            if (!proyectos.isEmpty()) {
                AID r = new AID();
                r.setLocalName("Estudiante");

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setSender(getAID());
                msg.addReceiver(r);
                msg.setLanguage(codec.getName());
                msg.setOntology(ontologia.getName());

                ObtenerEstudiantesDelProyecto oedp = new ObtenerEstudiantesDelProyecto();
                oedp.setId_proyectos(proyectos);

                try {
                    getContentManager().fillContent(msg, oedp);
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                }

                send(msg);
            }
        }
    }
    
    class MirarNuevasEntregas extends TickerBehaviour {
        public MirarNuevasEntregas(Agent a, long timer){
            super(a,timer);
        }
        
        @Override
        public void onTick(){
            //REQUEST
            String response = realizarRequest("assignments", null);

            response = response.substring(1, response.length() - 1);
            String[] aux_array = response.split(",");
            List entregas = new ArrayList();
            
            if (response.length() > 0) {
                for (String id : aux_array) {
                    entregas.add(Integer.parseInt(id));
                }
            }
            
            if (!entregas.isEmpty()) {
                AID r = new AID();
                r.setLocalName("Estudiante");

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setSender(getAID());
                msg.addReceiver(r);
                msg.setLanguage(codec.getName());
                msg.setOntology(ontologia.getName());

                ObtenerEstudiantesDeEntrega oede = new ObtenerEstudiantesDeEntrega();
                oede.setId_entregas(entregas);

                try {
                    getContentManager().fillContent(msg, oede);
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                }

                send(msg);
            }
        }
    }
    
    class MirarNuevosEquipos extends TickerBehaviour {
        public MirarNuevosEquipos(Agent a, long timer){
            super(a,timer);
        }
        @Override
        public void onTick(){
            //REQUEST
            String response = realizarRequest("teams", null);

            response = response.substring(1, response.length() - 1);
            String[] aux_array = response.split(",");
            List equipos = new ArrayList();
            
            if (response.length() > 0) {
                for (String id : aux_array) {
                    equipos.add(Integer.parseInt(id));
                }
            }
            
            if (!equipos.isEmpty()) {
                AID r = new AID();
                r.setLocalName("Estudiante");

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setSender(getAID());
                msg.addReceiver(r);
                msg.setLanguage(codec.getName());
                msg.setOntology(ontologia.getName());

                ObtenerEstudiantesDelEquipo oede = new ObtenerEstudiantesDelEquipo();
                oede.setId_equipos(equipos);

                try {
                    getContentManager().fillContent(msg, oede);
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                }

                send(msg);
            }
        }
    }
    
    class PlazoEntregaCumplido extends SimpleBehaviour {
        private boolean finished = false;
        private String fecha;
 
        public PlazoEntregaCumplido(Agent a, String fecha) {
            super(a);
            this.fecha = fecha;
        }
        
        @Override
        public void action() {
            String response = realizarRequest("assignments_date", this.fecha);

            JsonArray entregas = new JsonParser().parse(response).getAsJsonArray();
            
            if (entregas.size() > 0) {
                String mensaje, asunto;
            
                for (int i = 0; i < entregas.size(); i++) {
                    JsonObject entrega_object = entregas.get(i).getAsJsonObject();
                    Entrega entrega = new Entrega(entrega_object);
                    Proyecto proyecto = entrega.getProyecto();
                    List equipos = entrega.getEquipos();
                    
                    asunto = "Llegada de fecha limite para la Entrega #" + entrega.getId();
                            
                    mensaje = "Ha llegado la fecha limite para la siguiente entrega: \n" +
                              "Entrega #" + entrega.getId() + "\n" +
                              "    ID Proyecto: " + proyecto.getId() + "\n" +
                              "    Titulo del Proyecto: " + proyecto.getTitulo() + "\n" +
                              "    Enunciado: " + entrega.getEnunciado() + "\n" +
                              "    Fecha limite: " + entrega.getFecha() +  "\n\n" +
                              "Por favor entregarla hoy a mas tardar!";
                            
                    for (int j = 0; j < equipos.size(); j++) {
                        Equipo equipo = (Equipo) equipos.get(j);
                        List estudiantes = equipo.getEstudiantes();
                                
                        for (int k = 0; k < estudiantes.size(); k++) {
                            Estudiante estudiante = (Estudiante) estudiantes.get(k);
                                    
                            try {
                                SendEmail.generateAndSendEmail(asunto, estudiante.getCorreo(), mensaje);
                            } catch (MessagingException ex) {
                                Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            } else {
                System.out.println("No hay entregas para este día!");
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
    // Clase que describe el comportamiento que permite recibir un mensaje
    // y contestarlo
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
                if (msg == null) {
                    System.out.println("No message received");
                } else {
                    if (msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD) {
                        System.out.println("Mensaje NOT UNDERSTOOD recibido");
                    } else {
                        if (msg.getPerformative()== ACLMessage.INFORM) {
                            ContentElement ce = getContentManager().extractContent(msg);
                            
                            if (ce instanceof EstudiantesDelEquipoAlterado) {
                                EstudiantesDelEquipoAlterado edea = (EstudiantesDelEquipoAlterado) ce;
                                List equipos = edea.getEquipos();
                                
                                for (int i = 0; i < equipos.size(); i++) {
                                    Equipo equipo = (Equipo) equipos.get(i);
                                    List estudiantes = equipo.getEstudiantes();
                                    String asunto, mensaje;
                                    
                                    asunto = "Ha sido asignado al Equipo #" + equipo.getId() + "!";
                                    
                                    for (int j = 0; j < estudiantes.size(); j++) {
                                        Estudiante estudiante = (Estudiante) estudiantes.get(j);
                                        
                                        mensaje = "Se le ha asignado el Equipo #" + equipo.getId() + " con los siguientes compañeros: \n";
                                        
                                        for (int k = 0; k < estudiantes.size(); k++) {
                                            Estudiante aux_est = (Estudiante) estudiantes.get(k);
                                            
                                            if (j != k) {
                                                mensaje += "    Estudiante con cedula " + aux_est.getCedula() + "\n" +
                                                           "        Nombre: " + aux_est.getNombre() + " " + aux_est.getApellido() + "\n" +
                                                           "        Correo: " + aux_est.getCorreo() + "\n" +
                                                           "        Desempeño historico: " + aux_est.getDesempeno() + "\n";
                                            }
                                        }
                                        
                                        try {
                                            SendEmail.generateAndSendEmail(asunto, estudiante.getCorreo(), mensaje);
                                        } catch (MessagingException ex) {
                                            Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                            } else if (ce instanceof EstudiantesDelProyecto){
                                EstudiantesDelProyecto edp = (EstudiantesDelProyecto) ce;
                                List proyectos = edp.getProyectos();
                                
                                for (int i = 0; i < proyectos.size(); i++) {
                                    Proyecto proyecto = (Proyecto) proyectos.get(i);
                                    List equipos = proyecto.getEquipos();
                                    String asunto, mensaje;
                                    
                                    asunto = "Proyecto \"" + proyecto.getTitulo() + "\" asignado!";
                            
                                    mensaje = "Se le ha asignado el siguiente proyecto a su equipo: \n" +
                                              "Proyecto #" + proyecto.getId() + "\n" +
                                              "    Nombre: " + proyecto.getTitulo() + "\n" +
                                              "    Objetivo: " + proyecto.getObjetivo();
                                    
                                    for (int j = 0; j < equipos.size(); j++) {
                                        Equipo equipo = (Equipo) equipos.get(j);
                                        List estudiantes = equipo.getEstudiantes();
                                        
                                        for (int k = 0; k < estudiantes.size(); k++) {
                                            Estudiante estudiante = (Estudiante) estudiantes.get(k);
                                            
                                            try {
                                                SendEmail.generateAndSendEmail(asunto, estudiante.getCorreo(), mensaje);
                                            } catch (MessagingException ex) {
                                                Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }
                                }
                            } else if (ce instanceof EstudiantesDeEntrega){
                                EstudiantesDeEntrega edp = (EstudiantesDeEntrega) ce;
                                List entregas = edp.getEntregas();
                                
                                for (int i = 0; i < entregas.size(); i++) {
                                    Entrega entrega = (Entrega) entregas.get(i);
                                    Proyecto proyecto = entrega.getProyecto();
                                    List equipos = entrega.getEquipos();
                                    String asunto, mensaje;
                                    
                                    asunto = "Entrega \"" + entrega.getEnunciado() + "\" asignada!";
                            
                                    mensaje = "Entrega #" + entrega.getId() + "\n" +
                                              "    ID Proyecto: " + proyecto.getId() + "\n" +
                                              "    Titulo del Proyecto: " + proyecto.getTitulo() + "\n" +
                                              "    Enunciado: " + entrega.getEnunciado() + "\n" +
                                              "    Fecha limite: " + entrega.getFecha();
                                    
                                    for (int j = 0; j < equipos.size(); j++) {
                                        Equipo equipo = (Equipo) equipos.get(j);
                                        List estudiantes = equipo.getEstudiantes();
                                        
                                        for (int k = 0; k < estudiantes.size(); k++) {
                                            Estudiante estudiante = (Estudiante) estudiantes.get(k);
                                            
                                            try {
                                                SendEmail.generateAndSendEmail(asunto, estudiante.getCorreo(), mensaje);
                                            } catch (MessagingException ex) {
                                                Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    }
                                }
                            } else {
                                ACLMessage reply = msg.createReply();
                                reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                                reply.setContent("( UnexpectedContent (expected ping))");
                                
                                send(reply);
                            }
                        } else {
                            ACLMessage reply = msg.createReply();
                            reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                            reply.setContent("( (Unexpected-act "+ACLMessage.getPerformative(msg.getPerformative())+")( expected (inform)))");
                            
                            send(reply);
                        }
                    }
                }
            } catch (jade.content.lang.Codec.CodecException | jade.content.onto.OntologyException ce) {
                System.out.println(ce);
            }
        }
 
        @Override
        public boolean done() {
            return finished;
        }
    }
    
    private String realizarRequest(String modelo, String date) {
        String url;
        
        switch (modelo) {
            case "assignments_date":
                url = "http://apimasalarms.herokuapp.com/assignments/index/by_date?d=" + date ;
                break;
            default:
                url = "http://apimasalarms.herokuapp.com/" + modelo + "/index/differences";
                break;
        }
        
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