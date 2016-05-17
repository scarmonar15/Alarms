package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ObtenerEntregaCalificada
* @author ontology bean generator
* @version 2016/05/16, 20:34:47
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
