package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: ObtenerEstudiantesCalificados
* @author ontology bean generator
* @version 2016/05/9, 22:41:11
*/
public class ObtenerEstudiantesCalificados implements Predicate {

   /**
* Protege name: id_estudiantes
   */
   private List id_estudiantes = new ArrayList();
   public void addId_estudiantes(String elem) { 
     List oldList = this.id_estudiantes;
     id_estudiantes.add(elem);
   }
   public boolean removeId_estudiantes(String elem) {
     List oldList = this.id_estudiantes;
     boolean result = id_estudiantes.remove(elem);
     return result;
   }
   public void clearAllId_estudiantes() {
     List oldList = this.id_estudiantes;
     id_estudiantes.clear();
   }
   public Iterator getAllId_estudiantes() {return id_estudiantes.iterator(); }
   public List getId_estudiantes() {return id_estudiantes; }
   public void setId_estudiantes(List l) {id_estudiantes = l; }

}
