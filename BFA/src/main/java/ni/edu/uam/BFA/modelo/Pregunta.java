package ni.edu.uam.BFA.modelo;

import javax.persistence.*;

import ni.edu.uam.BFA.servicios.TestAptitud;
import org.openxava.annotations.*;

// Importaciones de Lombok
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pregunta")
@Views({
        @View(members = "idPregunta, numeroPregunta, tipoPregunta; enunciado; respuestaCorrecta; testAptitud"), // Vista original
        @View(name = "Simple", members = "numeroPregunta, enunciado") // Nueva vista para RespuestaSujeto
})
@Getter // Lombok: Genera todos los mtodos get() automticamente
@Setter // Lombok: Genera todos los mtodos set() automticamente
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer idPregunta;

    @Column(nullable = false, length = 500)
    @Required
    @Stereotype("MEMO")
    private String enunciado;

    @Column(name = "numero_pregunta", nullable = false)
    @Required
    private Integer numeroPregunta;

    @Column(name = "respuesta_correcta", nullable = false, length = 10)
    @Required
    private String respuestaCorrecta;

    @Column(name = "tipo_pregunta", nullable = false, length = 50)
    @Required
    private String tipoPregunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_test", nullable = false)
    @Required
    private TestAptitud testAptitud;

}