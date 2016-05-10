/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;
import alarmsOntology.*;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;
 
import jade.content.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.util.leap.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
 
public class Team extends Agent {
 
    private Codec codec = new SLCodec();
    private Ontology ontologia = AlarmsOntology.getInstance();
 
    // Clase que describe el comportamiento que permite recibir un mensaje
    // y contestarlo
    class RecibirDesempegnoHistorico extends SimpleBehaviour {
      private boolean finished = false;
 
      public RecibirDesempegnoHistorico(Agent a) {
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
                    if (ce instanceof EstudiantesDelEquipoAlterado){
                        // Recibido un INFORM con contenido correcto
                        EstudiantesDelEquipoAlterado edea = (EstudiantesDelEquipoAlterado) ce;
                        List estudiantes = edea.getEstudiantes();
                        System.out.println("*********** Desempeño recibido");
                        System.out.println("*********** Se han recibido los estudiantes del equipo "
                                + estudiantes.get(0));

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
    class ObtenerHistorico extends SimpleBehaviour{
        private boolean finished = false;
        public ObtenerHistorico(Agent a) {
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
                
                System.out.print("*********** Ingrese identificador del equipo: ");
                BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
                String respuesta = buff.readLine();
                
                ObtenerEstudiantesDelEquipo oee = new ObtenerEstudiantesDelEquipo();
                oee.setId_equipo(Integer.parseInt(respuesta));
                getContentManager().fillContent(msg, oee);
                send(msg);
                System.out.println("\n *************** Hemos registrado su equipo");
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
        RecibirDesempegnoHistorico PingBehaviour;
        ObtenerHistorico SenderBehaviour;
        SenderBehaviour = new ObtenerHistorico(this);
        PingBehaviour = new  RecibirDesempegnoHistorico(this);
        //addBehaviour(SenderBehaviour);
        //addBehaviour(PingBehaviour);
    }
}