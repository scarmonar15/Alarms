package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: EstudianteDenunciado
* @author ontology bean generator
* @version 2016/05/9, 20:41:35
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
