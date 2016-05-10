package agents;


import alarmsOntology.*;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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

public class Student extends Agent {

    private Codec codec = new SLCodec();
    private Ontology ontologia = AlarmsOntology.getInstance();

    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }

    class EnviarDenunciados extends SimpleBehaviour{
        private boolean finished = false;
        public EnviarDenunciados(Agent a) {
            super(a);
        }
        
        public void action(){
            MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchLanguage(codec.getName()),
                MessageTemplate.MatchOntology(ontologia.getName()));
            ACLMessage  msg = blockingReceive(mt);

            try {

                if(msg != null){
                if(msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD){
                    System.out.println("Mensaje NOT UNDERSTOOD recibido");
                }
                else{
                    if(msg.getPerformative()== ACLMessage.INFORM){
                    ContentElement ce = getContentManager().extractContent(msg);
                    if (ce instanceof ObtenerEstudianteDenunciado){
                        // Recibido un INFORM con contenido correcto
                        ACLMessage reply = msg.createReply();
                        
                        Equipo eq = new Equipo();
                        eq.setId(3);
                        
                        //Tendría que hacer un REQUEST al API preguntando por este estudiante
                        Estudiante e = new Estudiante();
                        ObtenerEstudianteDenunciado predicado = (ObtenerEstudianteDenunciado)ce;
                        e.setCedula(predicado.getId_estudiante()+"");
                        e.setNombre("Carlos");
                        e.setApellido("Bedoya");
                        e.setCorreo("sacarmonar@gmail.com");
                        e.setEquipo(eq);

                        EstudianteDenunciado ed = new EstudianteDenunciado();
                        ed.setEstudiante(e);
                        getContentManager().fillContent(reply, ed);
                        send(reply);
                        System.out.println("Enviando información básica del denunciado al Agente Profesor");
                    }else if(ce instanceof ObtenerEstudiantesDelEquipo){
                        ACLMessage reply = msg.createReply();
                        
                        Equipo eq = new Equipo();
                        ObtenerEstudiantesDelEquipo predicado = (ObtenerEstudiantesDelEquipo)ce;
                        eq.setId(predicado.getId_equipo());
                        
                        
                        
                        //Capacidad de desempeño histórico
                        EstudiantesDelEquipoAlterado edea = new EstudiantesDelEquipoAlterado();
                        edea.setEstudiantes(eq.getEstudiantes());
                        getContentManager().fillContent(reply, edea);
                        //send(reply);
                        System.out.println("*********** Enviando información histórica de estudiantes");
                        
                    }
                    else{
                        // Recibido un INFORM con contenido incorrecto
                        ACLMessage reply = msg.createReply();
                        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                        reply.setContent("( UnexpectedContent (expected ping))");
                        send(reply);
                    }
                }
                else {
                    // Recibida una performativa incorrecta
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                    reply.setContent("( (Unexpected-act "+ACLMessage.getPerformative(msg.getPerformative())+")( expected (inform)))");
                    send(reply);
                }
            }
            }else{
            //System.out.println("No message received");
            }

             }
             catch (jade.content.lang.Codec.CodecException ce) {
                   System.out.println(ce);
            }
            catch (jade.content.onto.OntologyException oe) {
                System.out.println(oe);
            } 
        }    
        
        public boolean done() {
            return true;
        }
    }
    
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

}
