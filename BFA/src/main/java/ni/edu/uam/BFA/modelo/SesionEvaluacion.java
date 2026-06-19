package ni.edu.uam.BFA.modelo;

import javax.persistence.*;
import java.util.*;
import org.openxava.annotations.*;

/**
 * Sesión de evaluación BFA.
 * Relaciona un Psicólogo con un Sujeto y contiene las respuestas.
 */
@Entity
@Table(name = "sesion_evaluacion")
@View(members = "idSesion, estado; fechaInicio, fechaFin; finalidad; "
        + "psicologo; sujeto; perfilBFA; respuestas")
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

    // ?? Relaciones ?????????????????????????????????????????????

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
    @CollectionView("RespuestaSujetoList")
    private List<RespuestaSujeto> respuestas = new ArrayList<>();

    // ?? Métodos de negocio ??????????????????????????????????????

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

    // ?? Getters & Setters ??????????????????????????????????????

    public Integer getIdSesion() { return idSesion; }
    public void setIdSesion(Integer idSesion) { this.idSesion = idSesion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public String getFinalidad() { return finalidad; }
    public void setFinalidad(String finalidad) { this.finalidad = finalidad; }

    public Psicologo getPsicologo() { return psicologo; }
    public void setPsicologo(Psicologo psicologo) { this.psicologo = psicologo; }

    public Sujeto getSujeto() { return sujeto; }
    public void setSujeto(Sujeto sujeto) { this.sujeto = sujeto; }

    public PerfilBFA getPerfilBFA() { return perfilBFA; }
    public void setPerfilBFA(PerfilBFA perfilBFA) { this.perfilBFA = perfilBFA; }

    public List<RespuestaSujeto> getRespuestas() { return respuestas; }
    public void setRespuestas(List<RespuestaSujeto> respuestas) { this.respuestas = respuestas; }
}

