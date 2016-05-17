package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ObtenerEstudiantesDeEntrega
* @author ontology bean generator
* @version 2016/05/16, 18:26:57
*/
public class ObtenerEstudiantesDeEntrega implements Predicate {

   /**
* Protege name: id_entregas
   */
   private List id_entregas = new ArrayList();
   public void addId_entregas(int elem) { 
     List oldList = this.id_entregas;
     id_entregas.add(elem);
   }

   public void clearAllId_entregas() {
     List oldList = this.id_entregas;
     id_entregas.clear();
   }
   public Iterator getAllId_entregas() {return id_entregas.iterator(); }
   public List getId_entregas() {return id_entregas; }
   public void setId_entregas(List l) {id_entregas = l; }

}
