package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Proyecto
* @author ontology bean generator
* @version 2016/05/9, 20:41:35
*/
public class Proyecto implements Concept {

   /**
* Protege name: titulo
   */
   private String titulo;
   public void setTitulo(String value) { 
    this.titulo=value;
   }
   public String getTitulo() {
     return this.titulo;
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

   /**
* Protege name: objetivo
   */
   private String objetivo;
   public void setObjetivo(String value) { 
    this.objetivo=value;
   }
   public String getObjetivo() {
     return this.objetivo;
   }

}
