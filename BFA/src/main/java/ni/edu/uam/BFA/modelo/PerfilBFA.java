package ni.edu.uam.BFA.modelo;


import javax.persistence.*;
import java.util.*;
import org.openxava.annotations.*;

/**
 * Perfil BFA generado al concluir una sesión.
 * Referencia 1 sesión y contiene 18 ResultadoFactor.
 */
@Entity
@Table(name = "perfil_bfa")
@Views({
        @View(members = "idPerfil, fechaGeneracion; observacionesGeneral; sesionEvaluacion; resultados"),
        @View(name = "Simple", members = "idPerfil, fechaGeneracion")
})
public class PerfilBFA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer idPerfil;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_generacion")
    private Date fechaGeneracion;

    @Column(name = "observaciones_general", length = 1000)
    @Stereotype("MEMO")
    private String observacionesGeneral;



    /** Sesión de la que proviene este perfil. */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sesion", nullable = false, unique = true)
    @Required
    @ReferenceView("Simple")
    private SesionEvaluacion sesionEvaluacion;

    /** 18 factores de resultado del perfil. */
    @OneToMany(mappedBy = "perfilBFA", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @CollectionView("ResultadoFactorList")
    private List<ResultadoFactor> resultados = new ArrayList<>();

    // ?? Getters & Setters ??????????????????????????????????????

    public Integer getIdPerfil() { return idPerfil; }
    public void setIdPerfil(Integer idPerfil) { this.idPerfil = idPerfil; }

    public Date getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(Date fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public String getObservacionesGeneral() { return observacionesGeneral; }
    public void setObservacionesGeneral(String observacionesGeneral) { this.observacionesGeneral = observacionesGeneral; }

    public SesionEvaluacion getSesionEvaluacion() { return sesionEvaluacion; }
    public void setSesionEvaluacion(SesionEvaluacion sesionEvaluacion) { this.sesionEvaluacion = sesionEvaluacion; }

    public List<ResultadoFactor> getResultados() { return resultados; }
    public void setResultados(List<ResultadoFactor> resultados) { this.resultados = resultados; }
}