package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ObtenerAsesorias
* @author ontology bean generator
* @version 2016/05/16, 21:13:33
*/
public class ObtenerAsesorias implements Predicate {

   /**
* Protege name: id_asesorias
   */
   private List id_asesorias = new ArrayList();
   public void addId_asesorias(int elem) { 
     List oldList = this.id_asesorias;
     id_asesorias.add(elem);
   }
   public void clearAllId_asesorias() {
     List oldList = this.id_asesorias;
     id_asesorias.clear();
   }
   public Iterator getAllId_asesorias() {return id_asesorias.iterator(); }
   public List getId_asesorias() {return id_asesorias; }
   public void setId_asesorias(List l) {id_asesorias = l; }

}
