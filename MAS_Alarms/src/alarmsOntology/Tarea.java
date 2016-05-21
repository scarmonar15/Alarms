package alarmsOntology;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import jade.content.*;
import jade.util.leap.*;

/**
* Protege name: Tarea
* @author ontology bean generator
* @version 2016/05/20, 20:23:14
*/
public class Tarea implements Concept {

    public Tarea() {}
    
    public Tarea(JsonObject json_object) {
        setId(json_object.get("id").getAsInt());
        setDescripcion(json_object.get("description").getAsString());
        
        JsonElement assignment = json_object.get("assignment");
        Entrega entrega = new Entrega(assignment);
        setEntrega(entrega);
    }
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
* Protege name: estudiantes
   */
   private List estudiantes = new ArrayList();
   public void addEstudiantes(Estudiante elem) { 
     List oldList = this.estudiantes;
     estudiantes.add(elem);
   }
   public boolean removeEstudiantes(Estudiante elem) {
     List oldList = this.estudiantes;
     boolean result = estudiantes.remove(elem);
     return result;
   }
   public void clearAllEstudiantes() {
     List oldList = this.estudiantes;
     estudiantes.clear();
   }
   public Iterator getAllEstudiantes() {return estudiantes.iterator(); }
   public List getEstudiantes() {return estudiantes; }
   public void setEstudiantes(List l) {estudiantes = l; }

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
