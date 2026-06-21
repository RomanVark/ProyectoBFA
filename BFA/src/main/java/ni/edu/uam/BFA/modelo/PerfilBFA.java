package ni.edu.uam.BFA.modelo;

import javax.persistence.*;
import java.util.*;

import ni.edu.uam.BFA.servicios.ResultadoFactor;
import ni.edu.uam.BFA.servicios.SesionEvaluacion;
import org.openxava.annotations.*;
import org.openxava.calculators.CurrentDateCalculator;

// Importaciones de Lombok
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "perfil_bfa")
@Views({
        @View(members = "idPerfil, fechaGeneracion; observacionesGeneral; sesionEvaluacion; resultados"),
        @View(name = "Simple", members = "fechaGeneracion") // La vista que pide ResultadoFactor
})
@Getter // Lombok: Genera todos los mtodos get() automticamente
@Setter // Lombok: Genera todos los mtodos set() automticamente
public class PerfilBFA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer idPerfil;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_generacion")
    @DefaultValueCalculator(CurrentDateCalculator.class)
    @ReadOnly
    private Date fechaGeneracion;

    @Column(name = "observaciones_general", length = 1000)
    @Stereotype("MEMO")
    private String observacionesGeneral;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_sesion", nullable = false, unique = true)
    @Required
    private SesionEvaluacion sesionEvaluacion;

    @OneToMany(mappedBy = "perfilBFA", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ResultadoFactor> resultados = new ArrayList<>();

}