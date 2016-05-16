package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ObtenerEstudiantesDelProyecto
* @author ontology bean generator
* @version 2016/05/16, 13:35:32
*/
public class ObtenerEstudiantesDelProyecto implements Predicate {

   /**
* Protege name: id_proyectos
   */
   private List id_proyectos = new ArrayList();
   public void addId_proyectos(int elem) { 
     List oldList = this.id_proyectos;
     id_proyectos.add(elem);
   }

   public void clearAllId_proyectos() {
     List oldList = this.id_proyectos;
     id_proyectos.clear();
   }
   public Iterator getAllId_proyectos() {return id_proyectos.iterator(); }
   public List getId_proyectos() {return id_proyectos; }
   public void setId_proyectos(List l) {id_proyectos = l; }

}
