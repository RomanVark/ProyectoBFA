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
 * Sesion de evaluaci?n BFA.
 * Relaciona un Psicologo con un Sujeto y contiene las respuestas.
 */
@Entity
@Table(name = "sesion_evaluacion")
@Views({
        @View(members = "idSesion, estado; fechaInicio, fechaFin; finalidad; "
                + "psicologo; sujeto; perfilBFA; respuestas"),
        @View(name = "Simple", members = "idSesion, estado, fechaInicio")
})
@Getter // Lombok: Genera todos los m?todos get() autom?ticamente
@Setter // Lombok: Genera todos los m?todos set() autom?ticamente
public class SesionEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    /** Psic?logo que administra la sesion (0..* sesiones por psic?logo). */
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

    /** Perfil BFA generado al concluir la sesion (puede ser null antes de finalizar). */
    @OneToOne(mappedBy = "sesionEvaluacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PerfilBFA perfilBFA;

    /** Respuestas registradas durante la sesion. */
    @OneToMany(mappedBy = "sesionEvaluacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RespuestaSujeto> respuestas = new ArrayList<>();

    // --- M?todos de negocio (Intactos) ---

    /**
     * Inicia la sesin: cambia estado a EN_PROCESO y registra fecha de inicio.
     * Llamado desde el diagrama de colaboracin (mensaje 1: iniciarEvaluacion).
     */
    public void iniciarEvaluacion() {
        this.estado = "EN_PROCESO";
        this.fechaInicio = new Date();
    }

    /**
     * Guarda la respuesta del sujeto a una pregunta.
     * Llamado desde el diagrama de colaboracion (mensaje 5: guardarRespuesta).
     */
    public void guardarRespuesta(RespuestaSujeto respuesta) {
        respuesta.setSesionEvaluacion(this);
        this.respuestas.add(respuesta);
    }

}