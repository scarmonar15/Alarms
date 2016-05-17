package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: EstudiantesDelEquipoAlterado
* @author ontology bean generator
* @version 2016/05/16, 20:34:47
*/
public class EstudiantesDelEquipoAlterado implements Predicate {

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

}
