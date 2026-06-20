package ni.edu.uam.BFA.servicios;
import javax.persistence.*;
import java.util.*;

import ni.edu.uam.BFA.modelo.Pregunta;
import org.openxava.annotations.*;

@Entity
@Table(name = "test_aptitud")
@Inheritance(strategy = InheritanceType.JOINED)
@View(members = "idTest, nombreTest, tipoTest; instrucciones; tiempoLimite; preguntas")
public class TestAptitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer idTest;

    @Column(name = "nombre_test", nullable = false, length = 100)
    @Required
    private String nombreTest;

    @Column(length = 500)
    @Stereotype("MEMO")
    private String instrucciones;

    @Column(name = "tiempo_limite")
    private Integer tiempoLimite;

    @Column(name = "tipo_test", nullable = false, length = 50)
    @Required
    private String tipoTest;


    @OneToMany(mappedBy = "testAptitud", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @CollectionView("PreguntaList")
    private List<Pregunta> preguntas = new ArrayList<>();



    public List<Pregunta> cargarTest() {
        return this.preguntas;
    }


    public List<Pregunta> obtenerPreguntas() {
        return Collections.unmodifiableList(this.preguntas);
    }


    public int calcularPuntajeDirecto(List<RespuestaSujeto> respuestas) {
        return (int) respuestas.stream()
                .filter(RespuestaSujeto::isEsCorrecta)
                .count();
    }



    public Integer getIdTest() { return idTest; }
    public void setIdTest(Integer idTest) { this.idTest = idTest; }

    public String getNombreTest() { return nombreTest; }
    public void setNombreTest(String nombreTest) { this.nombreTest = nombreTest; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }

    public Integer getTiempoLimite() { return tiempoLimite; }
    public void setTiempoLimite(Integer tiempoLimite) { this.tiempoLimite = tiempoLimite; }

    public String getTipoTest() { return tipoTest; }
    public void setTipoTest(String tipoTest) { this.tipoTest = tipoTest; }

    public List<Pregunta> getPreguntas() { return preguntas; }
    public void setPreguntas(List<Pregunta> preguntas) { this.preguntas = preguntas; }
}
