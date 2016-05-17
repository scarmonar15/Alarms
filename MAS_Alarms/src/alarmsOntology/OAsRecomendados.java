package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: OAsRecomendados
* @author ontology bean generator
* @version 2016/05/16, 20:34:47
*/
public class OAsRecomendados implements Predicate {

   /**
* Protege name: oas
   */
   private List oas = new ArrayList();
   public void addOas(ObjetoDeAprendizaje elem) { 
     List oldList = this.oas;
     oas.add(elem);
   }
   public boolean removeOas(ObjetoDeAprendizaje elem) {
     List oldList = this.oas;
     boolean result = oas.remove(elem);
     return result;
   }
   public void clearAllOas() {
     List oldList = this.oas;
     oas.clear();
   }
   public Iterator getAllOas() {return oas.iterator(); }
   public List getOas() {return oas; }
   public void setOas(List l) {oas = l; }

}
