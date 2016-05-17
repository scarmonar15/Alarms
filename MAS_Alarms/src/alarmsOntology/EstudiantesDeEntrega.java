package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: EstudiantesDeEntrega
* @author ontology bean generator
* @version 2016/05/16, 20:34:47
*/
public class EstudiantesDeEntrega implements Predicate {

   /**
* Protege name: entregas
   */
   private List entregas = new ArrayList();
   public void addEntregas(Entrega elem) { 
     List oldList = this.entregas;
     entregas.add(elem);
   }
   public boolean removeEntregas(Entrega elem) {
     List oldList = this.entregas;
     boolean result = entregas.remove(elem);
     return result;
   }
   public void clearAllEntregas() {
     List oldList = this.entregas;
     entregas.clear();
   }
   public Iterator getAllEntregas() {return entregas.iterator(); }
   public List getEntregas() {return entregas; }
   public void setEntregas(List l) {entregas = l; }

}
