package ni.edu.uam.BFA.servicios;

import javax.persistence.*;
import java.util.*;

import ni.edu.uam.BFA.modelo.PerfilBFA;
import ni.edu.uam.BFA.modelo.Psicologo;
import ni.edu.uam.BFA.modelo.Sujeto;
import org.openxava.annotations.*;

// Importaciones de Lombok
import lombok.Getter;
import lombok.Setter;

/**
 * Sesión de evaluación BFA.
 * Relaciona un Psicólogo con un Sujeto y contiene las respuestas.
 */
@Entity
@Table(name = "sesion_evaluacion")
@Views({
        @View(members = "idSesion, estado; fechaInicio, fechaFin; finalidad; "
                + "psicologo; sujeto; perfilBFA; respuestas"),
        @View(name = "Simple", members = "idSesion, estado, fechaInicio")
})
@Getter // Lombok: Genera todos los métodos get() automáticamente
@Setter // Lombok: Genera todos los métodos set() automáticamente
public class SesionEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sesion")
    @Hidden
    private Integer idSesion;

    /** p. ej. "INICIADA", "EN_PROCESO", "FINALIZADA" */
    @Column(nullable = false, length = 30)
    @Required
    private String estado;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_fin")
    private Date fechaFin;

    @Column(length = 255)
    private String finalidad;

    // --- Relaciones ---

    /** Psicólogo que administra la sesión (0..* sesiones por psicólogo). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_psicologo", nullable = false)
    @Required
    @ReferenceView("Simple")
    private Psicologo psicologo;

    /** Sujeto evaluado (0..* sesiones por sujeto). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sujeto", nullable = false)
    @Required
    @ReferenceView("Simple")
    private Sujeto sujeto;

    /** Perfil BFA generado al concluir la sesión (puede ser null antes de finalizar). */
    @OneToOne(mappedBy = "sesionEvaluacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PerfilBFA perfilBFA;

    /** Respuestas registradas durante la sesión. */
    @OneToMany(mappedBy = "sesionEvaluacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RespuestaSujeto> respuestas = new ArrayList<>();

    // --- Métodos de negocio (Intactos) ---

    /**
     * Inicia la sesión: cambia estado a EN_PROCESO y registra fecha de inicio.
     * Llamado desde el diagrama de colaboración (mensaje 1: iniciarEvaluacion).
     */
    public void iniciarEvaluacion() {
        this.estado = "EN_PROCESO";
        this.fechaInicio = new Date();
    }

    /**
     * Guarda la respuesta del sujeto a una pregunta.
     * Llamado desde el diagrama de colaboración (mensaje 5: guardarRespuesta).
     */
    public void guardarRespuesta(RespuestaSujeto respuesta) {
        respuesta.setSesionEvaluacion(this);
        this.respuestas.add(respuesta);
    }

}