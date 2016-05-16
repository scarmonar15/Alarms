package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: EntregaCalificada
* @author ontology bean generator
* @version 2016/05/16, 13:35:32
*/
public class EntregaCalificada implements Predicate {

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
* Protege name: nota
   */
   private float nota;
   public void setNota(float value) { 
    this.nota=value;
   }
   public float getNota() {
     return this.nota;
   }

}
