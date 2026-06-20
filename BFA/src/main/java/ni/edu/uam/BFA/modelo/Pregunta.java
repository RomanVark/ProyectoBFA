package ni.edu.uam.BFA.modelo;



import javax.persistence.*;

import ni.edu.uam.BFA.servicios.TestAptitud;
import org.openxava.annotations.*;


@Entity
@Table(name = "pregunta")
@View(members = "idPregunta, numeroPregunta, tipoPregunta; enunciado; respuestaCorrecta; testAptitud")
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
    private char respuestaCorrecta;

    @Column(name = "tipo_pregunta", nullable = false, length = 50)
    @Required
    private String tipoPregunta;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_test", nullable = false)
    @Required
    @ReferenceView("Simple")
    private TestAptitud testAptitud;


    public Integer getIdPregunta() { return idPregunta; }
    public void setIdPregunta(Integer idPregunta) { this.idPregunta = idPregunta; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public Integer getNumeroPregunta() { return numeroPregunta; }
    public void setNumeroPregunta(Integer numeroPregunta) { this.numeroPregunta = numeroPregunta; }

    public char getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(char respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }

    public String getTipoPregunta() { return tipoPregunta; }
    public void setTipoPregunta(String tipoPregunta) { this.tipoPregunta = tipoPregunta; }

    public TestAptitud getTestAptitud() { return testAptitud; }
    public void setTestAptitud(TestAptitud testAptitud) { this.testAptitud = testAptitud; }
}
