package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ObtenerEstudiantesDelEquipo
* @author ontology bean generator
* @version 2016/05/16, 19:19:29
*/
public class ObtenerEstudiantesDelEquipo implements Predicate {

   /**
* Protege name: id_equipos
   */
   private List id_equipos = new ArrayList();
   public void addId_equipos(int elem) { 
     List oldList = this.id_equipos;
     id_equipos.add(elem);
   }
   public void clearAllId_equipos() {
     List oldList = this.id_equipos;
     id_equipos.clear();
   }
   public Iterator getAllId_equipos() {return id_equipos.iterator(); }
   public List getId_equipos() {return id_equipos; }
   public void setId_equipos(List l) {id_equipos = l; }

}
