// file: AlarmsOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package alarmsOntology;

import jade.content.onto.*;
import jade.content.schema.*;
import jade.util.leap.HashMap;
import jade.content.lang.Codec;
import jade.core.CaseInsensitiveString;

/** file: AlarmsOntology.java
 * @author ontology bean generator
 * @version 2016/05/16, 19:19:29
 */
public class AlarmsOntology extends jade.content.onto.Ontology  {
  //NAME
  public static final String ONTOLOGY_NAME = "Alarms";
  // The singleton instance of this ontology
  private static ReflectiveIntrospector introspect = new ReflectiveIntrospector();
  private static Ontology theInstance = new AlarmsOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String ESTUDIANTESDELPROYECTO_PROYECTOS="proyectos";
    public static final String ESTUDIANTESDELPROYECTO="EstudiantesDelProyecto";
    public static final String OBTENERESTUDIANTESCALIFICADOS_ID_ESTUDIANTES="id_estudiantes";
    public static final String OBTENERESTUDIANTESCALIFICADOS="ObtenerEstudiantesCalificados";
    public static final String OBTENERESTUDIANTEDENUNCIADO_ID_ESTUDIANTE="id_estudiante";
    public static final String OBTENERESTUDIANTEDENUNCIADO="ObtenerEstudianteDenunciado";
    public static final String ESTUDIANTEDENUNCIADO_ESTUDIANTE="estudiante";
    public static final String ESTUDIANTEDENUNCIADO="EstudianteDenunciado";
    public static final String OBTENERENTREGACALIFICADA_NOTA="nota";
    public static final String OBTENERENTREGACALIFICADA_ID_ENTREGA="id_entrega";
    public static final String OBTENERENTREGACALIFICADA="ObtenerEntregaCalificada";
    public static final String OBTENERESTUDIANTESDELPROYECTO_ID_PROYECTOS="id_proyectos";
    public static final String OBTENERESTUDIANTESDELPROYECTO="ObtenerEstudiantesDelProyecto";
    public static final String ESTUDIANTESDELEQUIPOALTERADO_EQUIPOS="equipos";
    public static final String ESTUDIANTESDELEQUIPOALTERADO="EstudiantesDelEquipoAlterado";
    public static final String OBTENERESTUDIANTESDELEQUIPO_ID_EQUIPOS="id_equipos";
    public static final String OBTENERESTUDIANTESDELEQUIPO="ObtenerEstudiantesDelEquipo";
    public static final String ESTUDIANTESCALIFICADOS_ESTUDIANTES="estudiantes";
    public static final String ESTUDIANTESCALIFICADOS="EstudiantesCalificados";
    public static final String ESTUDIANTESDEENTREGA_ENTREGAS="entregas";
    public static final String ESTUDIANTESDEENTREGA="EstudiantesDeEntrega";
    public static final String ENTREGACALIFICADA_NOTA="nota";
    public static final String ENTREGACALIFICADA_ENTREGA="entrega";
    public static final String ENTREGACALIFICADA="EntregaCalificada";
    public static final String OBTENERESTUDIANTESDEENTREGA_ID_ENTREGAS="id_entregas";
    public static final String OBTENERESTUDIANTESDEENTREGA="ObtenerEstudiantesDeEntrega";
    public static final String PROYECTO_OBJETIVO="objetivo";
    public static final String PROYECTO_ENTREGAS="entregas";
    public static final String PROYECTO_EQUIPOS="equipos";
    public static final String PROYECTO_ID="id";
    public static final String PROYECTO_TITULO="titulo";
    public static final String PROYECTO="Proyecto";
    public static final String ENTREGA_ENUNCIADO="enunciado";
    public static final String ENTREGA_FECHA="fecha";
    public static final String ENTREGA_EQUIPOS="equipos";
    public static final String ENTREGA_ID="id";
    public static final String ENTREGA_PROYECTO="proyecto";
    public static final String ENTREGA_TAREAS="tareas";
    public static final String ENTREGA="Entrega";
    public static final String ESTUDIANTE_CEDULA="cedula";
    public static final String ESTUDIANTE_NOMBRE="nombre";
    public static final String ESTUDIANTE_EQUIPO="equipo";
    public static final String ESTUDIANTE_CORREO="correo";
    public static final String ESTUDIANTE_APELLIDO="apellido";
    public static final String ESTUDIANTE="Estudiante";
    public static final String TAREA_DESCRIPCION="descripcion";
    public static final String TAREA_ID="id";
    public static final String TAREA_ENTREGA="entrega";
    public static final String TAREA="Tarea";
    public static final String EQUIPO_ID="id";
    public static final String EQUIPO_ESTUDIANTES="estudiantes";
    public static final String EQUIPO="Equipo";

  /**
   * Constructor
  */
  private AlarmsOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema equipoSchema = new ConceptSchema(EQUIPO);
    add(equipoSchema, alarmsOntology.Equipo.class);
    ConceptSchema tareaSchema = new ConceptSchema(TAREA);
    add(tareaSchema, alarmsOntology.Tarea.class);
    ConceptSchema estudianteSchema = new ConceptSchema(ESTUDIANTE);
    add(estudianteSchema, alarmsOntology.Estudiante.class);
    ConceptSchema entregaSchema = new ConceptSchema(ENTREGA);
    add(entregaSchema, alarmsOntology.Entrega.class);
    ConceptSchema proyectoSchema = new ConceptSchema(PROYECTO);
    add(proyectoSchema, alarmsOntology.Proyecto.class);

    // adding AgentAction(s)

    // adding AID(s)

    // adding Predicate(s)
    PredicateSchema obtenerEstudiantesDeEntregaSchema = new PredicateSchema(OBTENERESTUDIANTESDEENTREGA);
    add(obtenerEstudiantesDeEntregaSchema, alarmsOntology.ObtenerEstudiantesDeEntrega.class);
    PredicateSchema entregaCalificadaSchema = new PredicateSchema(ENTREGACALIFICADA);
    add(entregaCalificadaSchema, alarmsOntology.EntregaCalificada.class);
    PredicateSchema estudiantesDeEntregaSchema = new PredicateSchema(ESTUDIANTESDEENTREGA);
    add(estudiantesDeEntregaSchema, alarmsOntology.EstudiantesDeEntrega.class);
    PredicateSchema estudiantesCalificadosSchema = new PredicateSchema(ESTUDIANTESCALIFICADOS);
    add(estudiantesCalificadosSchema, alarmsOntology.EstudiantesCalificados.class);
    PredicateSchema obtenerEstudiantesDelEquipoSchema = new PredicateSchema(OBTENERESTUDIANTESDELEQUIPO);
    add(obtenerEstudiantesDelEquipoSchema, alarmsOntology.ObtenerEstudiantesDelEquipo.class);
    PredicateSchema estudiantesDelEquipoAlteradoSchema = new PredicateSchema(ESTUDIANTESDELEQUIPOALTERADO);
    add(estudiantesDelEquipoAlteradoSchema, alarmsOntology.EstudiantesDelEquipoAlterado.class);
    PredicateSchema obtenerEstudiantesDelProyectoSchema = new PredicateSchema(OBTENERESTUDIANTESDELPROYECTO);
    add(obtenerEstudiantesDelProyectoSchema, alarmsOntology.ObtenerEstudiantesDelProyecto.class);
    PredicateSchema obtenerEntregaCalificadaSchema = new PredicateSchema(OBTENERENTREGACALIFICADA);
    add(obtenerEntregaCalificadaSchema, alarmsOntology.ObtenerEntregaCalificada.class);
    PredicateSchema estudianteDenunciadoSchema = new PredicateSchema(ESTUDIANTEDENUNCIADO);
    add(estudianteDenunciadoSchema, alarmsOntology.EstudianteDenunciado.class);
    PredicateSchema obtenerEstudianteDenunciadoSchema = new PredicateSchema(OBTENERESTUDIANTEDENUNCIADO);
    add(obtenerEstudianteDenunciadoSchema, alarmsOntology.ObtenerEstudianteDenunciado.class);
    PredicateSchema obtenerEstudiantesCalificadosSchema = new PredicateSchema(OBTENERESTUDIANTESCALIFICADOS);
    add(obtenerEstudiantesCalificadosSchema, alarmsOntology.ObtenerEstudiantesCalificados.class);
    PredicateSchema estudiantesDelProyectoSchema = new PredicateSchema(ESTUDIANTESDELPROYECTO);
    add(estudiantesDelProyectoSchema, alarmsOntology.EstudiantesDelProyecto.class);


    // adding fields
    equipoSchema.add(EQUIPO_ESTUDIANTES, estudianteSchema, 0, ObjectSchema.UNLIMITED);
    equipoSchema.add(EQUIPO_ID, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    tareaSchema.add(TAREA_ENTREGA, entregaSchema, ObjectSchema.MANDATORY);
    tareaSchema.add(TAREA_ID, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    tareaSchema.add(TAREA_DESCRIPCION, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    estudianteSchema.add(ESTUDIANTE_APELLIDO, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    estudianteSchema.add(ESTUDIANTE_CORREO, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    estudianteSchema.add(ESTUDIANTE_EQUIPO, equipoSchema, ObjectSchema.OPTIONAL);
    estudianteSchema.add(ESTUDIANTE_NOMBRE, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    estudianteSchema.add(ESTUDIANTE_CEDULA, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    entregaSchema.add(ENTREGA_TAREAS, tareaSchema, 0, ObjectSchema.UNLIMITED);
    entregaSchema.add(ENTREGA_PROYECTO, proyectoSchema, ObjectSchema.OPTIONAL);
    entregaSchema.add(ENTREGA_ID, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    entregaSchema.add(ENTREGA_EQUIPOS, equipoSchema, 0, ObjectSchema.UNLIMITED);
    entregaSchema.add(ENTREGA_FECHA, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    entregaSchema.add(ENTREGA_ENUNCIADO, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    proyectoSchema.add(PROYECTO_TITULO, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    proyectoSchema.add(PROYECTO_ID, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    proyectoSchema.add(PROYECTO_EQUIPOS, equipoSchema, 0, ObjectSchema.UNLIMITED);
    proyectoSchema.add(PROYECTO_ENTREGAS, entregaSchema, 0, ObjectSchema.UNLIMITED);
    proyectoSchema.add(PROYECTO_OBJETIVO, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.MANDATORY);
    obtenerEstudiantesDeEntregaSchema.add(OBTENERESTUDIANTESDEENTREGA_ID_ENTREGAS, (TermSchema)getSchema(BasicOntology.INTEGER), 1, ObjectSchema.UNLIMITED);
    entregaCalificadaSchema.add(ENTREGACALIFICADA_ENTREGA, entregaSchema, ObjectSchema.MANDATORY);
    entregaCalificadaSchema.add(ENTREGACALIFICADA_NOTA, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    estudiantesDeEntregaSchema.add(ESTUDIANTESDEENTREGA_ENTREGAS, entregaSchema, 0, ObjectSchema.UNLIMITED);
    estudiantesCalificadosSchema.add(ESTUDIANTESCALIFICADOS_ESTUDIANTES, estudianteSchema, 0, ObjectSchema.UNLIMITED);
    obtenerEstudiantesDelEquipoSchema.add(OBTENERESTUDIANTESDELEQUIPO_ID_EQUIPOS, (TermSchema)getSchema(BasicOntology.INTEGER), 1, ObjectSchema.UNLIMITED);
    estudiantesDelEquipoAlteradoSchema.add(ESTUDIANTESDELEQUIPOALTERADO_EQUIPOS, equipoSchema, 0, ObjectSchema.UNLIMITED);
    obtenerEstudiantesDelProyectoSchema.add(OBTENERESTUDIANTESDELPROYECTO_ID_PROYECTOS, (TermSchema)getSchema(BasicOntology.INTEGER), 1, ObjectSchema.UNLIMITED);
    obtenerEntregaCalificadaSchema.add(OBTENERENTREGACALIFICADA_ID_ENTREGA, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    obtenerEntregaCalificadaSchema.add(OBTENERENTREGACALIFICADA_NOTA, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.MANDATORY);
    estudianteDenunciadoSchema.add(ESTUDIANTEDENUNCIADO_ESTUDIANTE, estudianteSchema, ObjectSchema.MANDATORY);
    obtenerEstudianteDenunciadoSchema.add(OBTENERESTUDIANTEDENUNCIADO_ID_ESTUDIANTE, (TermSchema)getSchema(BasicOntology.INTEGER), ObjectSchema.MANDATORY);
    obtenerEstudiantesCalificadosSchema.add(OBTENERESTUDIANTESCALIFICADOS_ID_ESTUDIANTES, (TermSchema)getSchema(BasicOntology.STRING), 1, ObjectSchema.UNLIMITED);
    estudiantesDelProyectoSchema.add(ESTUDIANTESDELPROYECTO_PROYECTOS, proyectoSchema, 1, ObjectSchema.UNLIMITED);

    // adding name mappings

    // adding inheritance

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
  }
