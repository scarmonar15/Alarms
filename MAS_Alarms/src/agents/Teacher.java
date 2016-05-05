package agents;


import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class Teacher extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Container contenedor;
	int k;

	protected void setup() {

		// Printout a welcome message
		System.out.println("Hola Mundo.. el agente: " + getAID().getName()
				+ " esta listo.");

		// Enable O2A communication
		setEnabledO2ACommunication(true, 0);

		// Add the behaviour serving notifications from the external system
		this.addBehaviour(new CyclicBehaviour(this) {
		
			public void action() {
				System.out.println("ENTRO AL COMPORTAMIENTO");
				Container info = (Container) myAgent.getO2AObject();
				if (info != null) {
					contenedor = info;
					contenedor.mostrarMensaje("Entrego mensaje al contenedor");
					System.out.println();
				} else {
					block();
				}
			}
		});

	}

	public void controla(Container cont) {
		System.out.println("prueba");
	}

}
