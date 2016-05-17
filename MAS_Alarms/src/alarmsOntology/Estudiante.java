package alarmsOntology;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jade.content.*;

/**
* Protege name: Estudiante
* @author ontology bean generator
* @version 2016/05/16, 18:07:43
*/
public class Estudiante implements Concept {

    public Estudiante() {}
    
    public Estudiante(String json_doc) {
        JsonObject json_object = new JsonParser().parse(json_doc).getAsJsonObject();
        
        setCedula(json_object.get("id").getAsString());
        setNombre(json_object.get("name").getAsString());
        setApellido(json_object.get("last_name").getAsString());
        setCorreo(json_object.get("email").getAsString());
    }
    
    public Estudiante(JsonObject json_object) {        
        setCedula(json_object.get("id").getAsString());
        setNombre(json_object.get("name").getAsString());
        setApellido(json_object.get("last_name").getAsString());
        setCorreo(json_object.get("email").getAsString());
    }
   /**
* Protege name: apellido
   */
   private String apellido;
   public void setApellido(String value) { 
    this.apellido=value;
   }
   public String getApellido() {
     return this.apellido;
   }

   /**
* Protege name: correo
   */
   private String correo;
   public void setCorreo(String value) { 
    this.correo=value;
   }
   public String getCorreo() {
     return this.correo;
   }

   /**
* Protege name: equipo
   */
   private Equipo equipo;
   public void setEquipo(Equipo value) { 
    this.equipo=value;
   }
   public Equipo getEquipo() {
     return this.equipo;
   }

   /**
* Protege name: nombre
   */
   private String nombre;
   public void setNombre(String value) { 
    this.nombre=value;
   }
   public String getNombre() {
     return this.nombre;
   }

   /**
* Protege name: cedula
   */
   private String cedula;
   public void setCedula(String value) { 
    this.cedula=value;
   }
   public String getCedula() {
     return this.cedula;
   }

}
