package agents;

import alarmsOntology.*;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Student extends Agent {

    private final Codec codec = new SLCodec();
    private final Ontology ontologia = AlarmsOntology.getInstance();
    private final String USER_AGENT = "Mozilla/5.0";
    
    @Override
    protected void setup() {
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        
        sd.setType("student");
        sd.setName(getName());
        sd.setOwnership("ARNOIA");
        
        dfd.setName(getAID());
        dfd.addServices(sd);
        
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println(getLocalName() + " registration with DF unsucceeded. Reason: " + e.getMessage());
            doDelete();
        }

        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);

        EnviarDenunciados EnviarBehaviour = new EnviarDenunciados(this);
        addBehaviour(EnviarBehaviour);
    }
    
    
    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
    
    private String realizarRequest(String modelo, String id) {
        String url;
        
        if (modelo.equals("projects")) {
            url = "http://apimasalarms.herokuapp.com/" + modelo + "/" + id + "/students";
        } else {
            url = "http://apimasalarms.herokuapp.com/" + modelo + "/" + id + ".json";
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
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

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
    
    class EnviarDenunciados extends SimpleBehaviour {
        private boolean finished = false;
        
        public EnviarDenunciados(Agent a) {
            super(a);
        }
        
        public void action() {
            MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchLanguage(codec.getName()),
                MessageTemplate.MatchOntology(ontologia.getName())
            );
            
            ACLMessage msg = blockingReceive(mt);

            try {
                if (msg == null) {
                    System.out.println("No message received");
                } else {
                    if(msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD){
                        System.out.println("Message Not Understood!");
                    } else {
                        if (msg.getPerformative() == ACLMessage.INFORM) {
                            ContentElement ce = getContentManager().extractContent(msg);
                            
                            if (ce instanceof ObtenerEstudianteDenunciado){
                                // Recibido un INFORM con contenido correcto
                                ACLMessage reply = msg.createReply();
                                
                                //Tendría que hacer un REQUEST al API preguntando por este estudiante
                                ObtenerEstudianteDenunciado predicado = (ObtenerEstudianteDenunciado) ce;
                                
                                String id_estudiante = String.valueOf(predicado.getId_estudiante());
                                String response = realizarRequest("students", id_estudiante);
                                
                                Estudiante e = new Estudiante(response);
                                EstudianteDenunciado ed = new EstudianteDenunciado();
                                
                                ed.setEstudiante(e);
                                //System.out.println(ed.getEstudiante().getApellido());

                                getContentManager().fillContent(reply, ed);
                                send(reply);
                                
                                System.out.println("Enviando información básica del denunciado al Agente Profesor");
                            } else if (ce instanceof ObtenerEstudiantesCalificados){
                                ACLMessage reply = msg.createReply();
                                
                                ObtenerEstudiantesCalificados predicado = (ObtenerEstudiantesCalificados)ce;
                                List estudiantes =  predicado.getId_estudiantes();
                                EstudiantesCalificados ec = new EstudiantesCalificados();
                                
                                for (int i = 0; i < estudiantes.size(); i++){
                                    String response = realizarRequest("students", estudiantes.get(i).toString());
                                    Estudiante e = new Estudiante(response);
                                    ec.addEstudiantes(e);
                                }
                                
                                getContentManager().fillContent(reply, ec);
                                send(reply);
                            } else if (ce instanceof ObtenerEntregaCalificada) {
                                ACLMessage reply = msg.createReply();
                                
                                ObtenerEntregaCalificada predicado = (ObtenerEntregaCalificada) ce;
                                
                                //Request
                                String response = realizarRequest("assignments", String.valueOf(predicado.getId_entrega()));
                                
                                EntregaCalificada ec = new EntregaCalificada();
                                Entrega e = new Entrega(response);
                                
                                ec.setEntrega(e);
                                ec.setNota(predicado.getNota());
                                
                                getContentManager().fillContent(reply, ec);
                                send(reply);
                            } else if (ce instanceof ObtenerEstudiantesDelProyecto) {
                                ACLMessage reply = msg.createReply();
                                
                                ObtenerEstudiantesDelProyecto predicado = (ObtenerEstudiantesDelProyecto) ce;
                                List proyectos = new ArrayList();
                                String response;
                                
                                //Request
                                for (int i = 0; i < predicado.getId_proyectos().size(); i++) {
                                    response = realizarRequest("projects", String.valueOf(predicado.getId_proyectos().get(i)));
                                    Proyecto proyecto = new Proyecto(response);
                                    proyectos.add(proyecto);
                                }
                                
                                EstudiantesDelProyecto edp = new EstudiantesDelProyecto();
                                edp.setProyectos(proyectos);
                                
                                getContentManager().fillContent(reply, edp);
                                send(reply);
                            } else if (ce instanceof ObtenerEstudiantesDelEquipo) {
                                ACLMessage reply = msg.createReply();
                                
                                ObtenerEstudiantesDelEquipo predicado = (ObtenerEstudiantesDelEquipo) ce;
                                
                                //Request
                                //String response = realizarRequest("assignments", String.valueOf(predicado.getId_entrega()));
                                
                                EstudiantesDelEquipoAlterado edea = new EstudiantesDelEquipoAlterado();
                                System.out.println("ID Equipo Alterado Recibido: " + predicado.getId_equipo());
                                getContentManager().fillContent(reply, edea);
                                send(reply);
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
            } catch (jade.content.lang.Codec.CodecException | jade.content.onto.OntologyException e) {
                System.out.println(e);
            } 
        }    
        
        public boolean done() {
            return false;
        }
    }
    
    
}
