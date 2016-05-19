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

        RecibirDesempegnoHistorico PingBehaviour = new RecibirDesempegnoHistorico(this);
        MirarNuevosProyectos tickerProyectos = new MirarNuevosProyectos(this, 5000);
        MirarNuevasEntregas tickerEntregas = new MirarNuevasEntregas(this, 5000);
        MirarNuevosEquipos tickerNuevosEquipos = new MirarNuevosEquipos(this, 5000);
        
        //addBehaviour(tbf.wrap(tickerProyectos));
        //addBehaviour(tbf.wrap(tickerEntregas));
        //addBehaviour(tbf.wrap(tickerNuevosEquipos));
        addBehaviour(PingBehaviour);
    }
    
    class MirarNuevosProyectos extends TickerBehaviour {
        public MirarNuevosProyectos(Agent a, long timer){
            super(a,timer);
        }
        
        @Override
        public void onTick(){
            //REQUEST
            String response = realizarRequest("projects");
            
            response = response.substring(1, response.length() - 1);
            String[] aux_array = response.split(",");
            List proyectos = new ArrayList();
            
            if (response.length() > 0) {
                for (String id : aux_array) {
                    proyectos.add(Integer.parseInt(id));
                }
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
            String response = realizarRequest("assignments");

            response = response.substring(1, response.length() - 1);
            String[] aux_array = response.split(",");
            List entregas = new ArrayList();
            
            if (response.length() > 0) {
                for (String id : aux_array) {
                    entregas.add(Integer.parseInt(id));
                }
            }
            
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
            String response = realizarRequest("teams");

            response = response.substring(1, response.length() - 1);
            String[] aux_array = response.split(",");
            List equipos = new ArrayList();
            
            if (response.length() > 0) {
                for (String id : aux_array) {
                    equipos.add(Integer.parseInt(id));
                }
            }
            
            if (equipos.isEmpty()) {
                System.out.println("No hay nuevos equipos!");
            } else {
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
                        //Recibido un INFORM con contenido correcto
                        EstudiantesDelEquipoAlterado edea = (EstudiantesDelEquipoAlterado) ce;
                        List equipos = edea.getEquipos();
                        
                        System.out.println("*********** Se han agregado los siguientes nuevos equipos:");
                        
                        for (int i = 0; i < equipos.size(); i++) {
                            Equipo equipo = (Equipo) equipos.get(i);
                            List estudiantes = equipo.getEstudiantes();
                            
                            System.out.println("************************************************************");
                            System.out.println("Equipo #" + equipo.getId());
 
                            for (int j = 0; j < estudiantes.size(); j++) {
                                Estudiante estudiante = (Estudiante) estudiantes.get(j);
                                    
                                System.out.println("    Estudiante con cedula " + estudiante.getCedula());
                                System.out.println("        Nombre: " + estudiante.getNombre() + " " + estudiante.getApellido());
                                System.out.println("        Correo: " + estudiante.getCorreo());
                            }
                        }
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
                            System.out.println("    Objetivo: " + proyecto.getObjetivo());
                            System.out.println("    Lista de Grupos:");
                            
                            for (int j = 0; j < equipos.size(); j++) {
                                Equipo equipo = (Equipo) equipos.get(j);
                                List estudiantes = equipo.getEstudiantes();
                                
                                System.out.println("        Equipo #" + equipo.getId());
                                
                                for (int k = 0; k < estudiantes.size(); k++) {
                                    Estudiante estudiante = (Estudiante) estudiantes.get(k);
                                    
                                    System.out.println("            Estudiante con cedula " + estudiante.getCedula());
                                    System.out.println("                Nombre: " + estudiante.getNombre() + " " + estudiante.getApellido());
                                    System.out.println("                Correo: " + estudiante.getCorreo());
                                }
                            }
                        }
                        
                        System.out.println("************************************************************");
                    } else if (ce instanceof EstudiantesDeEntrega){
                        EstudiantesDeEntrega edp = (EstudiantesDeEntrega) ce;
                        List entregas = edp.getEntregas();
                        
                        System.out.println("*********** Se han agregado las siguientes nuevas entregas:");
                        
                        for (int i = 0; i < entregas.size(); i++) {
                            Entrega entrega = (Entrega) entregas.get(i);
                            List equipos = entrega.getEquipos();
                            
                            System.out.println("************************************************************");
                            System.out.println("Entrega #" + entrega.getId());
                            System.out.println("    Enunciado: " + entrega.getEnunciado());
                            System.out.println("    Fecha limite: " + entrega.getFecha());
                            System.out.println("    Lista de Grupos:");
                            
                            for (int j = 0; j < equipos.size(); j++) {
                                Equipo equipo = (Equipo) equipos.get(j);
                                List estudiantes = equipo.getEstudiantes();
                                
                                System.out.println("        Equipo #" + equipo.getId());
                                
                                for (int k = 0; k < estudiantes.size(); k++) {
                                    Estudiante estudiante = (Estudiante) estudiantes.get(k);
                                    
                                    System.out.println("            Estudiante con cedula " + estudiante.getCedula());
                                    System.out.println("                Nombre: " + estudiante.getNombre() + " " + estudiante.getApellido());
                                    System.out.println("                Correo: " + estudiante.getCorreo());
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
             catch (jade.content.lang.Codec.CodecException | jade.content.onto.OntologyException ce) {
                   System.out.println(ce);
            }
         }
 
      @Override
      public boolean done() {
        return finished;
      }
 
  } //Fin de la clase WaitPingAndReplyBehaviour
    
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