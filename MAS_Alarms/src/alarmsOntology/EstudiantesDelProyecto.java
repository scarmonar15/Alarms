package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: EstudiantesDelProyecto
* @author ontology bean generator
* @version 2016/05/16, 20:34:47
*/
public class EstudiantesDelProyecto implements Predicate {

   /**
* Protege name: proyectos
   */
   private List proyectos = new ArrayList();
   public void addProyectos(Proyecto elem) { 
     List oldList = this.proyectos;
     proyectos.add(elem);
   }
   public boolean removeProyectos(Proyecto elem) {
     List oldList = this.proyectos;
     boolean result = proyectos.remove(elem);
     return result;
   }
   public void clearAllProyectos() {
     List oldList = this.proyectos;
     proyectos.clear();
   }
   public Iterator getAllProyectos() {return proyectos.iterator(); }
   public List getProyectos() {return proyectos; }
   public void setProyectos(List l) {proyectos = l; }

}
