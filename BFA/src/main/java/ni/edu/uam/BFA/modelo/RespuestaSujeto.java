package ni.edu.uam.BFA.modelo;

import javax.persistence.*;
import java.util.Date;
import org.openxava.annotations.*;

/**
 * Respuesta de un sujeto a una pregunta dentro de una sesión.
 *
 * Mensaje 4 del diagrama: responderPregunta() crea esta entidad.
 * Mensaje 5: guardarRespuesta() la persiste a través de SesionEvaluacion.
 */
@Entity
@Table(name = "respuesta_sujeto")
@View(members = "idRespuesta, estado; esCorrecta; respuestaMarcada; "
        + "fechaInicio; sesionEvaluacion; pregunta")
public class RespuestaSujeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer idRespuesta;

    /** true si la respuesta marcada coincide con la respuesta correcta. */
    @Column(name = "es_correcta", nullable = false)
    private boolean esCorrecta;

    /** p. ej. "RESPONDIDA", "OMITIDA", "TACHADA" */
    @Column(nullable = false, length = 30)
    @Required
    private String estado;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    /**
     * Letra marcada por el sujeto: A, B, C, D o E.
     * Puede ser nula si la pregunta fue omitida.
     */
    @Column(name = "respuesta_marcada")
    private Character respuestaMarcada;

    // ?? Relaciones ?????????????????????????????????????????????

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sesion", nullable = false)
    @Required
    @ReferenceView("Simple")
    private SesionEvaluacion sesionEvaluacion;

    /** Pregunta a la que corresponde esta respuesta. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pregunta", nullable = false)
    @Required
    @ReferenceView("Simple")
    private Pregunta pregunta;

    // ?? Lógica al marcar respuesta ??????????????????????????????

    /**
     * Registra la letra marcada y evalúa si es correcta automáticamente.
     * Mensaje 4 del diagrama de colaboración: responderPregunta().
     */
    public void responderPregunta(char letra) {
        this.respuestaMarcada = letra;
        this.fechaInicio = new Date();
        this.estado = "RESPONDIDA";
        this.esCorrecta = (pregunta != null)
                && (pregunta.getRespuestaCorrecta() == letra);
    }

    // ?? Getters & Setters ??????????????????????????????????????

    public Integer getIdRespuesta() { return idRespuesta; }
    public void setIdRespuesta(Integer idRespuesta) { this.idRespuesta = idRespuesta; }

    public boolean isEsCorrecta() { return esCorrecta; }
    public void setEsCorrecta(boolean esCorrecta) { this.esCorrecta = esCorrecta; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Character getRespuestaMarcada() { return respuestaMarcada; }
    public void setRespuestaMarcada(Character respuestaMarcada) { this.respuestaMarcada = respuestaMarcada; }

    public SesionEvaluacion getSesionEvaluacion() { return sesionEvaluacion; }
    public void setSesionEvaluacion(SesionEvaluacion sesionEvaluacion) { this.sesionEvaluacion = sesionEvaluacion; }

    public Pregunta getPregunta() { return pregunta; }
    public void setPregunta(Pregunta pregunta) { this.pregunta = pregunta; }
}
