package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: EstudiantesDelProyecto
* @author ontology bean generator
* @version 2016/05/9, 20:42:21
*/
public class EstudiantesDelProyecto implements Predicate {

   /**
* Protege name: estudiantes
   */
   private List estudiantes = new ArrayList();
   public void addEstudiantes(Estudiante elem) { 
     List oldList = this.estudiantes;
     estudiantes.add(elem);
   }
   public boolean removeEstudiantes(Estudiante elem) {
     List oldList = this.estudiantes;
     boolean result = estudiantes.remove(elem);
     return result;
   }
   public void clearAllEstudiantes() {
     List oldList = this.estudiantes;
     estudiantes.clear();
   }
   public Iterator getAllEstudiantes() {return estudiantes.iterator(); }
   public List getEstudiantes() {return estudiantes; }
   public void setEstudiantes(List l) {estudiantes = l; }

   /**
* Protege name: proyecto
   */
   private Proyecto proyecto;
   public void setProyecto(Proyecto value) { 
    this.proyecto=value;
   }
   public Proyecto getProyecto() {
     return this.proyecto;
   }

}
