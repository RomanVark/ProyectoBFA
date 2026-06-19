package ni.edu.uam.BFA.modelo;


import org.openxava.annotations.Hidden;
import org.openxava.annotations.ReferenceView;
import org.openxava.annotations.Required;
import org.openxava.annotations.View;
import org.openxava.annotations.Views;

import javax.persistence.*;
import javax.persistence.*;

@Entity
@Table(name = "resultado_factor")
@Views({
        @View(members = "perfilBFA; accionimoFactor; puntajeDirecto; percentil; puntuacionTipica"),
        @View(name = "Simple", members = "accionimFactor, puntajeDirecto")
})
public class ResultadoFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer idResultado;


    @Column(name = "accionimo_factor", nullable = false, length = 50)
    @Required
    private String accionimFactor;

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



    public void generarResultado(int pd) {
        this.puntajeDirecto = pd;
        // TODO: Implementar conversión a percentil y puntuación típica
        //       usando tabla de baremo según norma de aplicación BFA.
        this.percentil = convertirAPercentil(pd);
        this.puntuacionTipica = convertirAPuntuacionTipica(this.percentil);
    }

    private int convertirAPercentil(int pd) {
        // Placeholder ? sustituir con tabla de baremo real
        return Math.min(pd * 2, 99);
    }

    private int convertirAPuntuacionTipica(int percentil) {
        // Conversión percentil ? estanino (1-9)
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



    public Integer getIdResultado() { return idResultado; }
    public void setIdResultado(Integer idResultado) { this.idResultado = idResultado; }

    public String getAccionimFactor() { return accionimFactor; }
    public void setAccionimFactor(String accionimFactor) { this.accionimFactor = accionimFactor; }

    public Integer getPuntajeDirecto() { return puntajeDirecto; }
    public void setPuntajeDirecto(Integer puntajeDirecto) { this.puntajeDirecto = puntajeDirecto; }

    public Integer getPercentil() { return percentil; }
    public void setPercentil(Integer percentil) { this.percentil = percentil; }

    public Integer getPuntuacionTipica() { return puntuacionTipica; }
    public void setPuntuacionTipica(Integer puntuacionTipica) { this.puntuacionTipica = puntuacionTipica; }

    public PerfilBFA getPerfilBFA() { return perfilBFA; }
    public void setPerfilBFA(PerfilBFA perfilBFA) { this.perfilBFA = perfilBFA; }
}
