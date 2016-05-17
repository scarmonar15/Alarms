package alarmsOntology;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jade.content.*;
import jade.util.leap.*;

/**
* Protege name: Equipo
* @author ontology bean generator
* @version 2016/05/16, 18:07:43
*/
public class Equipo implements Concept {

    public Equipo() {}
    
    public Equipo(String json_doc) {
        JsonObject json_object = new JsonParser().parse(json_doc).getAsJsonObject();
        
        setId(json_object.get("id").getAsInt());
        
        JsonArray students_array = json_object.get("students").getAsJsonArray();
        List est = new ArrayList();
        
        for (JsonElement student : students_array) {
            Estudiante stud = new Estudiante(student.getAsJsonObject());
            est.add(stud);
        }
        
        setEstudiantes(est);
    }
    
    public Equipo(JsonObject json_object) {
        setId(json_object.get("id").getAsInt());
        
        JsonArray students_array = json_object.get("students").getAsJsonArray();
        List est = new ArrayList();
        
        for (JsonElement student : students_array) {
            Estudiante stud = new Estudiante(student.getAsJsonObject());
            est.add(stud);
        }
        
        setEstudiantes(est);
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

}
