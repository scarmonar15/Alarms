package org.ibrow.alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: Estudiante
* @author ontology bean generator
* @version 2016/05/9, 18:01:44
*/
public class Estudiante implements Concept {

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
