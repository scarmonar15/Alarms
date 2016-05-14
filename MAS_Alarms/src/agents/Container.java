package agents;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.ArrayList;
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
        new Container().mainMenu();
    }
    
    public void mainMenu() throws IOException{
        System.out.println("1. Denunciar a un estudiante \n2. Calificar una entrega");
        while (true) {
            BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
            String respuesta = buff.readLine();
            if (respuesta.equals("1")) {
                new Container().denunciarEstudiante();
                break;
            } else if (respuesta.equals("2")) {
                new Container().calificarUnaEntrega();
                break;
            } else {
                System.out.println("El número seleccionado no se encuentra dentro de las opciones");
            }
        }
    }
    public void denunciarEstudiante() throws IOException{
        System.out.println("Has elegido Denunciar a un estudiante");
        Object object[] = new Object[1];
        object[0] = new String("Denuncia");
        try{
            AgentController prof = c.createNewAgent("Profesor2", "agents.Teacher", object);
            prof.start();
        } catch (ControllerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mainMenu();
    }
    
    public void calificarUnaEntrega() throws IOException{
        System.out.println("Has elegido Calificar una Entrega y eres un profesor");
        try{
            Object object[] = new Object[1];
            object[0] =  new String("Calificacion");
            AgentController prof = c.createNewAgent("Profesor3", "agents.Teacher",object);
            prof.start();
        }catch (ControllerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mainMenu();
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
