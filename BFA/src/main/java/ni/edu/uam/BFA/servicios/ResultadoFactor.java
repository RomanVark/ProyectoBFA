package ni.edu.uam.BFA.servicios;

import ni.edu.uam.BFA.modelo.PerfilBFA;

// Corregido: Usando el @Required correcto de OpenXava
import org.openxava.annotations.Required;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.ReferenceView;
import org.openxava.annotations.View;

import javax.persistence.*;

// Importaciones de Lombok
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "resultado_factor")
@View(members = "perfilBFA; accionimoFactor; puntajeDirecto; percentil; puntuacionTipica")
@Getter // Lombok: Genera los getters
@Setter // Lombok: Genera los setters
public class ResultadoFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer idResultado;

    @Column(name = "accionimo_factor", nullable = false, length = 50)
    @Required
    private String accionimoFactor;

    @Column(name = "puntaje_directo")
    private Integer puntajeDirecto;

    @Column
    private Integer percentil;

    @Column(name = "puntuacion_tipica")
    private Integer puntuacionTipica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_perfil", nullable = false)
    @Required
    @ReferenceView("Simple")
    private PerfilBFA perfilBFA;

    // --- L?gica de Negocio (Lombok no toca esto, se mantiene intacto) ---

    public void generarResultado(int pd) {
        this.puntajeDirecto = pd;
        // TODO: Implementar conversi?n a percentil y puntuaci?n t?pica
        //       usando tabla de baremo seg?n norma de aplicaci?n BFA.
        this.percentil = convertirAPercentil(pd);
        this.puntuacionTipica = convertirAPuntuacionTipica(this.percentil);
    }

    private int convertirAPercentil(int pd) {
        return Math.min(pd * 2, 99);
    }

    private int convertirAPuntuacionTipica(int percentil) {
        // Conversion percentil ? estanino (1-9)
        if (percentil < 5)  return 1;
        if (percentil < 11) return 2;
        if (percentil < 23) return 3;
        if (percentil < 40) return 4;
        if (percentil < 60) return 5;
        if (percentil < 77) return 6;
        if (percentil < 89) return 7;
        if (percentil < 95) return 8;
        return 9;
    }

}