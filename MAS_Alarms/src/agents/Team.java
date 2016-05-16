/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;
import alarmsOntology.*;
import com.google.gson.Gson;
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
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
 
public class Team extends Agent {
 
    private final Codec codec = new SLCodec();
    private final Ontology ontologia = AlarmsOntology.getInstance();
    private final ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();
    private final String USER_AGENT = "Mozilla/5.0";
    
    @Override
    protected void setup() {
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontologia);
        
        RecibirDesempegnoHistorico PingBehaviour;
        ObtenerHistorico SenderBehaviour;
        
        SenderBehaviour = new ObtenerHistorico(this);
        PingBehaviour = new  RecibirDesempegnoHistorico(this);
        MirarProyecto tickerProyecto = new MirarProyecto(this, 10000);
        MirarNuevosEquipos tickerNuevosEquipos = new MirarNuevosEquipos(this, 5000);
        
        addBehaviour(tbf.wrap(tickerProyecto));
        //addBehaviour(tbf.wrap(tickerNuevosEquipos));
        //addBehaviour(SenderBehaviour);
        addBehaviour(PingBehaviour);
    }
    
    class MirarProyecto extends TickerBehaviour{
        public MirarProyecto(Agent a, long timer){
            super(a,timer);
        }
        
        @Override
        public void onTick(){
            //REQUEST
            String response = realizarRequest("projects");
            
            response = response.substring(1, response.length() - 1);
            String[] aux_array = response.split(",");
            List proyectos = new ArrayList();
            
            for (String id : aux_array) {
                proyectos.add(Integer.parseInt(id));
            }
            
            if (proyectos.isEmpty()) {
                System.out.println("No hay nuevos proyectos!");
            } else {
                AID r = new AID();
                r.setLocalName("Estudiante");

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setSender(getAID());
                msg.addReceiver(r);
                msg.setLanguage(codec.getName());
                msg.setOntology(ontologia.getName());

                ObtenerEstudiantesDelProyecto oede = new ObtenerEstudiantesDelProyecto();
                oede.setId_proyectos(proyectos);

                try {
                    getContentManager().fillContent(msg, oede);
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                }

                send(msg);
            }
        }
    }
    
    class MirarNuevosEquipos extends TickerBehaviour{
        public MirarNuevosEquipos(Agent a, long timer){
            super(a,timer);
        }
        @Override
        public void onTick(){
            //REQUEST
            String response = realizarRequest("assignments");
            
            Gson gson = new Gson();
            ArrayList entregas = gson.fromJson(response, ArrayList.class);
            
            if (entregas.isEmpty()) {
                System.out.println("No hay nuevas entregas!");
            } else {
                AID r = new AID();
                r.setLocalName("Estudiante");

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setSender(getAID());
                msg.addReceiver(r);
                msg.setLanguage(codec.getName());
                msg.setOntology(ontologia.getName());

                ObtenerEstudiantesDelEquipo oede = new ObtenerEstudiantesDelEquipo();
                oede.setId_equipo(1);

                try {
                    getContentManager().fillContent(msg, oede);
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(Team.class.getName()).log(Level.SEVERE, null, ex);
                }

                send(msg);
            }
        }
    }
    // Clase que describe el comportamiento que permite recibir un mensaje
    // y contestarlo
    class RecibirDesempegnoHistorico extends SimpleBehaviour {
      private boolean finished = false;
 
      public RecibirDesempegnoHistorico(Agent a) {
        super(a);
      }
 
      @Override
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
                        /*EstudiantesDelEquipoAlterado edea = (EstudiantesDelEquipoAlterado) ce;
                        List estudiantes = edea.getEstudiantes();
                        System.out.println("*********** Desempeño recibido");
                        System.out.println("*********** Se han recibido los estudiantes del equipo "
                                + estudiantes.get(0));*/
                        System.out.println("Estudiantes Del Equipo Recibidos");

                    } else if (ce instanceof EstudiantesDelProyecto){
                        EstudiantesDelProyecto edp = (EstudiantesDelProyecto) ce;
                        List proyectos = edp.getProyectos();
                        
                        System.out.println("*********** Se han agregado los siguientes nuevos proyectos:");
                        
                        for (int i = 0; i < proyectos.size(); i++) {
                            Proyecto proyecto = (Proyecto) proyectos.get(i);
                            List equipos = proyecto.getEquipos();
                            
                            System.out.println("************************************************************");
                            System.out.println("Proyecto #" + proyecto.getId());
                            System.out.println("    Nombre: " + proyecto.getTitulo());
                            System.out.println("    Objetivo" + proyecto.getObjetivo());
                            System.out.println("    Lista de Grupos:");
                            
                            for (int j = 0; j < equipos.size(); j++) {
                                Equipo equipo = (Equipo) equipos.get(j);
                                List estudiantes = equipo.getEstudiantes();
                                
                                System.out.println("        Grupo #" + equipo.getId());
                                
                                for (int k = 0; k < estudiantes.size(); k++) {
                                    Estudiante estudiante = (Estudiante) estudiantes.get(k);
                                    
                                    System.out.println("        Estudiante con cedula " + estudiante.getCedula());
                                    System.out.println("            Nombre: " + estudiante.getNombre() + " " + estudiante.getApellido());
                                    System.out.println("            Correo: " + estudiante.getCorreo());
                                }
                            }
                        }
                        
                        System.out.println("************************************************************");
                    } else {
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
}