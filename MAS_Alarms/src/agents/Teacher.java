/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;
import alarmsOntology.*;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPANames;
import jade.domain.DFService;
import jade.domain.FIPAException;
 
import jade.content.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
 
public class Teacher extends Agent {
 
    private Codec codec = new SLCodec();
    private Ontology ontologia = AlarmsOntology.getInstance();
 
    // Clase que describe el comportamiento que permite recibir un mensaje
    // y contestarlo
    class WaitPingAndReplyBehaviour extends SimpleBehaviour {
      private boolean finished = false;
 
      public WaitPingAndReplyBehaviour(Agent a) {
        super(a);
      }
 
      public void action() {
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
                    if (ce instanceof EstudianteDenunciado){
                        // Recibido un INFORM con contenido correcto
                        EstudianteDenunciado ed = (EstudianteDenunciado) ce;
                        Estudiante e = ed.getEstudiante();
                        System.out.println("Denuncia recibida");
                        System.out.println("Se ha hecho una denuncia de un estudiante"
                                + " con cédula: " + e.getCedula() + " llamado " + e.getNombre() + " " + e.getApellido() + " con correo"
                                + " " + e.getCorreo());

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
        return finished;
      }
 
  } //Fin de la clase WaitPingAndReplyBehaviour
    class ObtenerAlDenunciado extends SimpleBehaviour{
        private boolean finished = false;
        public ObtenerAlDenunciado(Agent a) {
            super(a);
        }

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
                System.out.println("\n Hemos registrado su denuncia");
            } catch (Exception e) {
                System.out.println(e);
                finished = true;
            }
        }

        public boolean done() {
            return true;
        }
    }
    protected void setup() {
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);
        WaitPingAndReplyBehaviour PingBehaviour;
        ObtenerAlDenunciado SenderBehaviour;
        SenderBehaviour = new ObtenerAlDenunciado(this);
        PingBehaviour = new  WaitPingAndReplyBehaviour(this);
        addBehaviour(SenderBehaviour);
        addBehaviour(PingBehaviour);
    }
}