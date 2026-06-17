package ni.edu.uam.BFA.modelo;
import javax.persistence.*;
import java.util.*;
import org.openxava.annotations.*;


@Entity
@Table(name = "perfil_bfa")
@View(members = "idPerfil, fechaGeneracion; observacionesGeneral; sesionEvaluacion; resultados")
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



    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sesion", nullable = false, unique = true)
    @Required
    @ReferenceView("Simple")
    private SesionEvaluacion sesionEvaluacion;

    @OneToMany(mappedBy = "perfilBFA", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @CollectionView("ResultadoFactorList")
    private List<ResultadoFactor> resultados = new ArrayList<>();



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
