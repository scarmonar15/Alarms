package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: EstudianteDenunciado
* @author ontology bean generator
* @version 2016/05/16, 19:19:29
*/
public class EstudianteDenunciado implements Predicate {

   /**
* Protege name: estudiante
   */
   private Estudiante estudiante;
   public void setEstudiante(Estudiante value) { 
    this.estudiante=value;
   }
   public Estudiante getEstudiante() {
     return this.estudiante;
   }

}
