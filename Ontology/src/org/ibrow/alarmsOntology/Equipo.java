package org.ibrow.alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Equipo
* @author ontology bean generator
* @version 2016/05/9, 18:01:44
*/
public class Equipo implements Concept {

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
