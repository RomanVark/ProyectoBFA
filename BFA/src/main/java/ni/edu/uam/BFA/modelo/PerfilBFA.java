package ni.edu.uam.BFA.modelo;

import javax.persistence.*;
import java.util.*;

import ni.edu.uam.BFA.servicios.ResultadoFactor;
import ni.edu.uam.BFA.servicios.SesionEvaluacion;
import org.openxava.annotations.*;

// Importaciones de Lombok
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "perfil_bfa")
@View(members = "idPerfil, fechaGeneracion; observacionesGeneral; sesionEvaluacion; resultados")
@Getter // Lombok: Genera todos los métodos get() automáticamente
@Setter // Lombok: Genera todos los métodos set() automáticamente
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

}