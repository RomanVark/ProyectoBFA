package ni.edu.uam.BFA.servicios;

import javax.persistence.*;
import java.util.Date;

import ni.edu.uam.BFA.modelo.Pregunta;
import org.openxava.annotations.*;

// Importaciones de Lombok
import lombok.Getter;
import lombok.Setter;

/**
 * Respuesta de un sujeto a una pregunta dentro de una sesión.
 */
@Entity
@Table(name = "respuesta_sujeto")
@View(members = "idRespuesta, estado; esCorrecta; respuestaMarcada; "
        + "fechaInicio; sesionEvaluacion; pregunta")
@Getter // Lombok: Genera todos los métodos get() automáticamente
@Setter // Lombok: Genera todos los métodos set() automáticamente
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
    @Column(name = "respuesta_marcada", length = 10)
    private String respuestaMarcada;

    // --- Relaciones ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sesion", nullable = false)
    @Required
    @ReferenceView("Simple")
    private SesionEvaluacion sesionEvaluacion;

    /** Pregunta a la que corresponde esta respuesta. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pregunta", nullable = false)
    @Required
    @ReferenceView("Simple") // Esta ya la arreglamos en el paso anterior (Pregunta.java)
    private Pregunta pregunta;

    // --- Lógica al marcar respuesta ---

    /**
     * Registra la letra marcada y evalúa si es correcta automáticamente.
     * Mensaje 4 del diagrama de colaboración: responderPregunta().
     */
    // CAMBIO 2: Recibimos un String en lugar de char
    public void responderPregunta(String letra) {
        this.respuestaMarcada = letra;
        this.fechaInicio = new Date();
        this.estado = "RESPONDIDA";

        this.esCorrecta = (pregunta != null)
                && (letra != null)
                && (pregunta.getRespuestaCorrecta().equals(letra));
    }

}