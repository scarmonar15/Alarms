package agents;


import alarmsOntology.*;
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
            try{
                AID r = new AID();
                r.setLocalName("Profesor");
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setSender(getAID());
                msg.addReceiver(r);
                msg.setLanguage(codec.getName());
                msg.setOntology(ontologia.getName());
                
                Equipo eq = new Equipo();
                eq.setId(3);
                
                Estudiante e = new Estudiante();
                e.setCedula("1020304050");
                e.setNombre("Carlos");
                e.setApellido("Bedoya");
                e.setCorreo("sacarmonar@gmail.com");
                e.setEquipo(eq);
     /*           
                List estudiantes = new ArrayList();
                estudiantes.add(e);
                eq.setEstudiantes(estudiantes);*/
                
                EstudianteDenunciado ed = new EstudianteDenunciado();
                ed.setEstudiante(e);
                getContentManager().fillContent(msg, ed);
                send(msg);
                System.out.println("Estudiante denunciado enviado al profesor");
            }catch(Exception e){
                System.out.println(e);
                finished = true;
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
