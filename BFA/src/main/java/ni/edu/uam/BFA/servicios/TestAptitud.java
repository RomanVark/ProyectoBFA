package ni.edu.uam.BFA.servicios;

import javax.persistence.*;
import java.util.*;

import ni.edu.uam.BFA.modelo.Pregunta;
import org.openxava.annotations.*;

// Importaciones de Lombok
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "test_aptitud")
@Inheritance(strategy = InheritanceType.JOINED)
@View(members = "idTest, nombreTest, tipoTest; instrucciones; tiempoLimite; preguntas")
@Getter // Lombok: Genera todos los métodos get() automáticamente
@Setter // Lombok: Genera todos los métodos set() automáticamente
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

    // --- Métodos de lógica de negocio (Intactos) ---

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

}