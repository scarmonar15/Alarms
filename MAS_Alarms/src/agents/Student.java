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
        
        switch (modelo) {
            case "projects":
                url = "http://apimasalarms.herokuapp.com/" + modelo + "/" + id + "/students";
                break;
            case "teams":
                url = "http://apimasalarms.herokuapp.com/" + modelo + "/" + id + "/students";
                break;
            case "info_assignment":
                url = "http://apimasalarms.herokuapp.com/assignments/" + id + "/students";
                break;
            case "info_student":
                url = "http://apimasalarms.herokuapp.com/students/" + id + ".json";
                break;
            case "learnings":
                url = "http://apimasalarms.herokuapp.com/learnings/" + id + "/students";
                break;
            case "counselings":
                url = "http://apimasalarms.herokuapp.com/counselings/" + id + "/students";
                break;
            default:
                url = "http://apimasalarms.herokuapp.com/" + modelo + "/" + id + ".json";
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
                                String response = realizarRequest("info_assignment", String.valueOf(predicado.getId_entrega()));
                                
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
                            } else if (ce instanceof ObtenerEstudiantesDeEntrega) {
                                ACLMessage reply = msg.createReply();
                                
                                ObtenerEstudiantesDeEntrega predicado = (ObtenerEstudiantesDeEntrega) ce;
                                List entregas = new ArrayList();
                                String response;
                                
                                //Request
                                for (int i = 0; i < predicado.getId_entregas().size(); i++) {
                                    response = realizarRequest("info_assignment", String.valueOf(predicado.getId_entregas().get(i)));
                                    Entrega entrega = new Entrega(response);
                                    entregas.add(entrega);
                                }
                                
                                EstudiantesDeEntrega ede = new EstudiantesDeEntrega();
                                ede.setEntregas(entregas);
                                
                                getContentManager().fillContent(reply, ede);
                                send(reply);
                            } else if (ce instanceof ObtenerEstudiantesDelEquipo) {
                                ACLMessage reply = msg.createReply();
                                
                                ObtenerEstudiantesDelEquipo predicado = (ObtenerEstudiantesDelEquipo) ce;
                                List equipos = new ArrayList();
                                String response;
                                
                                //Request
                                for (int i = 0; i < predicado.getId_equipos().size(); i++) {
                                    response = realizarRequest("teams", String.valueOf(predicado.getId_equipos().get(i)));
                                    Equipo equipo = new Equipo(response);
                                    equipos.add(equipo);
                                }
                                
                                EstudiantesDelEquipoAlterado ede = new EstudiantesDelEquipoAlterado();
                                ede.setEquipos(equipos);
                                
                                getContentManager().fillContent(reply, ede);
                                send(reply);
                            } else if (ce instanceof ObtenerObjetosDeAprendizaje) {
                                ACLMessage reply = msg.createReply();
                                
                                ObtenerObjetosDeAprendizaje predicado = (ObtenerObjetosDeAprendizaje) ce;
                                List oas = new ArrayList();
                                String response;
                                
                                //Request
                                for (int i = 0; i < predicado.getId_OAs().size(); i++) {
                                    response = realizarRequest("learnings", String.valueOf(predicado.getId_OAs().get(i)));
                                    ObjetoDeAprendizaje oa = new ObjetoDeAprendizaje(response);
                                    oas.add(oa);
                                }
                                
                                OAsRecomendados oasr = new OAsRecomendados();
                                oasr.setOas(oas);
                                
                                getContentManager().fillContent(reply, oasr);
                                send(reply);
                            } else if (ce instanceof ObtenerAsesorias) {
                                ACLMessage reply = msg.createReply();
                                
                                ObtenerAsesorias predicado = (ObtenerAsesorias) ce;
                                List asesorias = new ArrayList();
                                String response;
                                
                                //Request
                                for (int i = 0; i < predicado.getId_asesorias().size(); i++) {
                                    response = realizarRequest("counselings", String.valueOf(predicado.getId_asesorias().get(i)));
                                    Asesoria asesoria = new Asesoria(response);
                                    asesorias.add(asesoria);
                                }
                                
                                AsesoriasProgramadas ap = new AsesoriasProgramadas();
                                ap.setAsesorias(asesorias);
                                
                                getContentManager().fillContent(reply, ap);
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
                System.out.println(e);System.out.println("gola");
            } 
        }    
        
        public boolean done() {
            return false;
        }
    }
    
    
}
