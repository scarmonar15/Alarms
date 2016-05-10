package agents;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;

public class Container {

    final public int pi = 3;
    String men;

    public static void main(String[] args) {
        new Container().crearContenedor();
    }

    public void crearContenedor() {
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
        ContainerController c = rt.createMainContainer(p);

        //crear contenedor
        Profile p2 = new ProfileImpl();
        p.setParameter(Profile.MAIN_HOST, "localhost");
        p.setParameter(Profile.MAIN_PORT, "1099");
        p.setParameter(Profile.LOCAL_HOST, "localhost");
        ContainerController contenedor = rt.createAgentContainer(p2);

        try {
            //Crear el agente
            System.out.println(c.getContainerName().toString());
            AgentController stud = c.createNewAgent("Estudiante", "agents.Student", null);
            AgentController teac = c.createNewAgent("Profesor", "agents.Teacher", null);
            AgentController team = c.createNewAgent("Equipo", "agents.Team", null);

            stud.start();
            team.start();
            teac.start();
            
            AgentController pi = c.getAgent("Equipo");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            pi.putO2AObject(this, false);
            System.out.println();



            // try {
            // ContainerController c = new ContainerController(cp, impl,
            // platformName)
            // .getPlatformController().kill();
        } catch (ControllerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
        men = mensaje;
        System.out.println();
    }
}
