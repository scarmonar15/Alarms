package agents;

import alarmsOntology.AlarmsOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.AID;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Container {
    static Teacher zapata;
    static ContainerController c;

    public static void main(String[] args) throws IOException {
        new Container().crearContenedor();
        System.out.println("1. Denunciar a un estudiante \n2. Calificar una entrega");
        while (true) {
            BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
            String respuesta = buff.readLine();
            if (respuesta.equals("1")) {
                new Container().denunciarEstudiante();
                break;
            } else if (respuesta.equals("2")) {
                System.out.println("Has elegido calificar una entrega");
                break;
            } else {
                System.out.println("El número seleccionado no se encuentra dentro de las opciones");
            }
        }
    }
    public void denunciarEstudiante(){
        System.out.println("Has elegido Denunciar a un estudiante");
        try{
            AgentController prof = c.createNewAgent("Profesor2", "agents.Teacher", null);
            prof.start();
        } catch (ControllerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void crearContenedor() throws IOException {
        Runtime rt = Runtime.instance();

        //Crear plataforma principal
        Profile p = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "1099");
        p.setParameter(Profile.GUI, "true");
        p.setParameter(Profile.PLATFORM_ID, "Plataforma");
        p.setParameter(Profile.LOCAL_HOST, "localhost");
        p.setParameter(Profile.AGENTS, "rma:jade.tools.rma.rma");
        p.setParameter(Profile.CONTAINER_NAME, "ContenedorPrincipal");
        c = rt.createMainContainer(p);

        try {
            //Crear el agente
            AgentController stud = c.createNewAgent("Estudiante", "agents.Student", null);
            //AgentController teac = c.createNewAgent("Profesor", "agents.Teacher", null);
            AgentController team = c.createNewAgent("Equipo", "agents.Team", null);
            
            stud.start();
            team.start();
            //teac.start();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            // try {
            // ContainerController c = new ContainerController(cp, impl,
            // platformName)
            // .getPlatformController().kill();
        } catch (ControllerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
