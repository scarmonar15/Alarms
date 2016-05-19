package agents;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Container {
    static ContainerController c;
    static int prof_cont = 1, team_cont = 1;

    public static void main(String[] args) throws IOException {
        new Container().crearContenedor();
        new Container().mainMenu();
    }
    public void mainMenu() throws IOException{
        System.out.println("1. Denunciar a un estudiante \n" + 
                           "2. Calificar una entrega \n" + 
                           "3. Simular la llegada de una fecha limite");
        
        OUTER:
        while (true) {
            BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
            String respuesta = buff.readLine();
            System.out.println();
            
            switch (respuesta) {
                case "1":
                    seleccionMenu("1");
                    break OUTER;
                case "2":
                    seleccionMenu("2");
                    break OUTER;
                case "3":
                    seleccionMenu("3");
                    break OUTER;
                default:
                    System.out.println("El número seleccionado no se encuentra dentro de las opciones");
                    break;
            }
        }
    }
    
    public void seleccionMenu(String seleccion) {
        Object[] decision = new Object[2];
        
        switch (seleccion) {
            case "1":
                decision[0] = "Denunciar";
                break;
            case "2":
                decision[0] = "Calificar";
                break;
            case "3":
                BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
                String fecha = "";
                
                System.out.println("Ingrese la fecha que desea simular en formato AAAA-MM-DD");
        
                try {
                    fecha = buff.readLine();
                } catch (IOException ex) {
                    Logger.getLogger(Container.class.getName()).log(Level.SEVERE, null, ex);
                }

                decision[0] = "SimularFecha";
                decision[1] = fecha;
                
                break;
        }
        
        try {
            AgentController prof = c.createNewAgent("Profesor" + prof_cont, "agents.Teacher", decision);
            prof.start();
            prof_cont++;
            
            if (decision[0] == "SimularFecha") {
                AgentController team = c.createNewAgent("Equipo" + team_cont, "agents.Team", decision);
                team.start();
                team_cont++;
            }
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
            AgentController teac = c.createNewAgent("Profesor", "agents.Teacher", null);
            AgentController team = c.createNewAgent("Equipo", "agents.Team", null);
            
            stud.start();
            team.start();
            teac.start();

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
