package alarmsOntology;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Asesoria
* @author ontology bean generator
* @version 2016/05/16, 21:13:33
*/
public class Asesoria implements Concept {

    public Asesoria() {}
    
    public Asesoria(String json_doc) {
        JsonObject json_object = new JsonParser().parse(json_doc).getAsJsonObject();
        
        setId(json_object.get("id").getAsInt());
        setAsesor(json_object.get("adviser").getAsString());
        setFecha(json_object.get("date").getAsString());
        setSalon(json_object.get("classroom").getAsString());
        
        JsonArray students_array = json_object.get("students").getAsJsonArray();
        List est = new ArrayList();
        
        for (JsonElement student : students_array) {
            Estudiante estudiante = new Estudiante(student.getAsJsonObject());
            est.add(estudiante);
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

   /**
* Protege name: salon
   */
   private String salon;
   public void setSalon(String value) { 
    this.salon=value;
   }
   public String getSalon() {
     return this.salon;
   }

   /**
* Protege name: asesor
   */
   private String asesor;
   public void setAsesor(String value) { 
    this.asesor=value;
   }
   public String getAsesor() {
     return this.asesor;
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

}
