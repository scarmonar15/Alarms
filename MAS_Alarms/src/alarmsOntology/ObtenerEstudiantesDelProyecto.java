package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ObtenerEstudiantesDelProyecto
* @author ontology bean generator
* @version 2016/05/9, 20:42:21
*/
public class ObtenerEstudiantesDelProyecto implements Predicate {

   /**
* Protege name: id_equipo
   */
   private int id_equipo;
   public void setId_equipo(int value) { 
    this.id_equipo=value;
   }
   public int getId_equipo() {
     return this.id_equipo;
   }

   /**
* Protege name: id_proyecto
   */
   private int id_proyecto;
   public void setId_proyecto(int value) { 
    this.id_proyecto=value;
   }
   public int getId_proyecto() {
     return this.id_proyecto;
   }

}
