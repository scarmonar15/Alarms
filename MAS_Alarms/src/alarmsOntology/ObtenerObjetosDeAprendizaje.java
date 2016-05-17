package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ObtenerObjetosDeAprendizaje
* @author ontology bean generator
* @version 2016/05/16, 20:34:47
*/
public class ObtenerObjetosDeAprendizaje implements Predicate {

   /**
* Protege name: id_OAs
   */
   private List id_OAs = new ArrayList();
   public void addId_OAs(int elem) { 
     List oldList = this.id_OAs;
     id_OAs.add(elem);
   }
   public void clearAllId_OAs() {
     List oldList = this.id_OAs;
     id_OAs.clear();
   }
   public Iterator getAllId_OAs() {return id_OAs.iterator(); }
   public List getId_OAs() {return id_OAs; }
   public void setId_OAs(List l) {id_OAs = l; }

}
