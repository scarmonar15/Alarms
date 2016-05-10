package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Entrega
* @author ontology bean generator
* @version 2016/05/9, 22:41:11
*/
public class Entrega implements Concept {

   /**
* Protege name: tareas
   */
   private List tareas = new ArrayList();
   public void addTareas(Tarea elem) { 
     List oldList = this.tareas;
     tareas.add(elem);
   }
   public boolean removeTareas(Tarea elem) {
     List oldList = this.tareas;
     boolean result = tareas.remove(elem);
     return result;
   }
   public void clearAllTareas() {
     List oldList = this.tareas;
     tareas.clear();
   }
   public Iterator getAllTareas() {return tareas.iterator(); }
   public List getTareas() {return tareas; }
   public void setTareas(List l) {tareas = l; }

   /**
* Protege name: proyecto
   */
   private Proyecto proyecto;
   public void setProyecto(Proyecto value) { 
    this.proyecto=value;
   }
   public Proyecto getProyecto() {
     return this.proyecto;
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
* Protege name: fecha
   */
   private String fecha;
   public void setFecha(String value) { 
    this.fecha=value;
   }
   public String getFecha() {
     return this.fecha;
   }

   /**
* Protege name: enunciado
   */
   private String enunciado;
   public void setEnunciado(String value) { 
    this.enunciado=value;
   }
   public String getEnunciado() {
     return this.enunciado;
   }

}
