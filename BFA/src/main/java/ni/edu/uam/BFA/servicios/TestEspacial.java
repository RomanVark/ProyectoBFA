package ni.edu.uam.BFA.servicios;

import javax.persistence.*;

import org.openxava.annotations.*;

// Importaciones de Lombok
import lombok.Getter;
import lombok.Setter;

/**
 * Test Espacial ? Desplazamiento (subclase de TestAptitud).
 *
 * Especializa el c?lculo de puntaje para la l?gica de rotaci?n/desplazamiento
 * de figuras definida en el documento Test_Espacial__Desplazamiento_.docx.
 *
 * Tiempo est?ndar: 5 minutos (300 s), ?tems 30-49.
 */
@Entity
@Table(name = "test_espacial")
@PrimaryKeyJoinColumn(name = "id_test")
@Views({
        // Vista por defecto (cuando entras a ver el test completo)
        @View(members = "nombreTest, tipoTest; tiempoLimite; tipoMovimiento; instrucciones; preguntas"),

        // Vista Simple (la que OpenXava busca al mostrar el combo en la pantalla Pregunta)
        @View(name = "Simple", members = "nombreTest, tipoMovimiento")
})
@Getter // Lombok: Genera los m?todos get()
@Setter // Lombok: Genera los m?todos set()
public class TestEspacial extends TestAptitud {

    /**
     * Tipo de movimiento evaluado.
     * p. ej. "DESPLAZAMIENTO", "ROTACION", "INVERSION"
     */
    @Column(name = "tipo_movimiento", nullable = false, length = 50)
    @Required
    private String tipoMovimiento;

    public TestEspacial() {
        // Estos m?todos funcionan perfectamente gracias al @Setter
        // que le pusimos a la clase padre (TestAptitud)
        setNombreTest("Test Espacial ? Desplazamiento");
        setTipoTest("ESPACIAL");
        setTiempoLimite(300); // 5 minutos seg?n instrucciones
        this.tipoMovimiento = "DESPLAZAMIENTO";
    }

    /**
     * Calcula el puntaje directo para el test espacial.
     * Sobreescribe la logica base: solo cuenta items 30-49.
     *
     * En el BFA Espacial, cada item puede tener mas de una respuesta correcta
     * (p. ej. item 28 ? A y C).  Esta implementacion delega a la superclase
     * que ya cuenta por esCorrecta=true.
     */
    @Override
    public int calcularPuntajeDirecto(java.util.List<RespuestaSujeto> respuestas) {
        return super.calcularPuntajeDirecto(respuestas);
    }

}