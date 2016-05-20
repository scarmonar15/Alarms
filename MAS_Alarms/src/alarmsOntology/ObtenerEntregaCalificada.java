package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ObtenerEntregaCalificada
* @author ontology bean generator
* @version 2016/05/20, 16:21:52
*/
public class ObtenerEntregaCalificada implements Predicate {

   /**
* Protege name: id_entrega
   */
   private int id_entrega;
   public void setId_entrega(int value) { 
    this.id_entrega=value;
   }
   public int getId_entrega() {
     return this.id_entrega;
   }

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
