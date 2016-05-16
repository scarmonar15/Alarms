package alarmsOntology;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jade.content.*;
import jade.util.leap.*;

/**
* Protege name: Proyecto
* @author ontology bean generator
* @version 2016/05/16, 13:35:32
*/
public class Proyecto implements Concept {

    public Proyecto() {}
    
    public Proyecto(String json_doc) {
        JsonObject json_object = new JsonParser().parse(json_doc).getAsJsonObject();
        
        setId(json_object.get("id").getAsInt());
        setTitulo(json_object.get("title").getAsString());
        setObjetivo(json_object.get("description").getAsString());
        
        JsonArray teams_array = json_object.get("teams").getAsJsonArray();
        List nuevos_equipos = new ArrayList();
        
        for (JsonElement team : teams_array) {
            Equipo nuevo_equipo = new Equipo(team.getAsJsonObject());
            nuevos_equipos.add(nuevo_equipo);
        }
        
        setEquipos(nuevos_equipos);
    }
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
