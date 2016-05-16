package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Tarea
* @author ontology bean generator
* @version 2016/05/16, 13:35:32
*/
public class Tarea implements Concept {

   /**
* Protege name: entrega
   */
   private Entrega entrega;
   public void setEntrega(Entrega value) { 
    this.entrega=value;
   }
   public Entrega getEntrega() {
     return this.entrega;
   }

   /**
* Protege name: id
   */
   private int id;
   public void setId(int value) { 
    this.id=value;
   }
   public int getId() {
     return this.id;
   }

   /**
* Protege name: descripcion
   */
   private String descripcion;
   public void setDescripcion(String value) { 
    this.descripcion=value;
   }
   public String getDescripcion() {
     return this.descripcion;
   }

}
