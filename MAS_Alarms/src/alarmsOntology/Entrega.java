package alarmsOntology;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jade.content.*;
import jade.util.leap.*;

/**
* Protege name: Entrega
* @author ontology bean generator
* @version 2016/05/16, 18:26:57
*/
public class Entrega implements Concept {

    public Entrega() {}
    
    public Entrega(String json_doc) {
        JsonObject json_object = new JsonParser().parse(json_doc).getAsJsonObject();
        
        setId(Integer.parseInt(json_object.get("id").getAsString()));
        setFecha(json_object.get("limit_date").getAsString());
        setEnunciado(json_object.get("description").getAsString());
        
        JsonObject project = json_object.get("project").getAsJsonObject();
        Proyecto proyecto = new Proyecto(project);
        setProyecto(proyecto);
        
        JsonArray teams_array = json_object.get("teams").getAsJsonArray();
        List nuevos_equipos = new ArrayList();
        
        for (JsonElement team : teams_array) {
            Equipo nuevo_equipo = new Equipo(team.getAsJsonObject());
            nuevos_equipos.add(nuevo_equipo);
        }
        
        setEquipos(nuevos_equipos);
    }
    
    public Entrega(JsonObject json_object) {
        setId(Integer.parseInt(json_object.get("id").getAsString()));
        setFecha(json_object.get("limit_date").getAsString());
        setEnunciado(json_object.get("description").getAsString());
        
        JsonObject project = json_object.get("project").getAsJsonObject();
        Proyecto proyecto = new Proyecto(project);
        setProyecto(proyecto);
        
        JsonArray teams_array = json_object.get("teams").getAsJsonArray();
        List nuevos_equipos = new ArrayList();
        
        for (JsonElement team : teams_array) {
            Equipo nuevo_equipo = new Equipo(team.getAsJsonObject());
            nuevos_equipos.add(nuevo_equipo);
        }
        
        setEquipos(nuevos_equipos);
    }
    
    public Entrega(JsonElement json_element) {
        JsonObject json_object = json_element.getAsJsonObject();
        
        setId(Integer.parseInt(json_object.get("id").getAsString()));
        setFecha(json_object.get("limit_date").getAsString());
        setEnunciado(json_object.get("description").getAsString());
        
        JsonObject project = json_object.get("project").getAsJsonObject();
        Proyecto proyecto = new Proyecto(project);
        setProyecto(proyecto);
    }
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
* Protege name: equipos
   */
   private List equipos = new ArrayList();
   public void addEquipos(Equipo elem) { 
     List oldList = this.equipos;
     equipos.add(elem);
   }
   public boolean removeEquipos(Equipo elem) {
     List oldList = this.equipos;
     boolean result = equipos.remove(elem);
     return result;
   }
   public void clearAllEquipos() {
     List oldList = this.equipos;
     equipos.clear();
   }
   public Iterator getAllEquipos() {return equipos.iterator(); }
   public List getEquipos() {return equipos; }
   public void setEquipos(List l) {equipos = l; }

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
