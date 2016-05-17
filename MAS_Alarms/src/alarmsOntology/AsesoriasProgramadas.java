package alarmsOntology;


import jade.content.*;
import jade.util.leap.*;
import jade.core.*;

/**
* Protege name: AsesoriasProgramadas
* @author ontology bean generator
* @version 2016/05/16, 21:13:33
*/
public class AsesoriasProgramadas implements Predicate {

   /**
* Protege name: asesorias
   */
   private List asesorias = new ArrayList();
   public void addAsesorias(Asesoria elem) { 
     List oldList = this.asesorias;
     asesorias.add(elem);
   }
   public boolean removeAsesorias(Asesoria elem) {
     List oldList = this.asesorias;
     boolean result = asesorias.remove(elem);
     return result;
   }
   public void clearAllAsesorias() {
     List oldList = this.asesorias;
     asesorias.clear();
   }
   public Iterator getAllAsesorias() {return asesorias.iterator(); }
   public List getAsesorias() {return asesorias; }
   public void setAsesorias(List l) {asesorias = l; }

}
