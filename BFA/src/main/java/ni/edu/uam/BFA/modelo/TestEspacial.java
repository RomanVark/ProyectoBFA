package ni.edu.uam.BFA.modelo;

import javax.persistence.*;

import org.htmlunit.xpath.operations.String;
import org.openxava.annotations.*;


/**
 * Test Espacial ? Desplazamiento (subclase de TestAptitud).
 *
 * Especializa el cálculo de puntaje para la lógica de rotación/desplazamiento
 * de figuras definida en el documento Test_Espacial__Desplazamiento_.docx.
 *
 * Tiempo estándar: 5 minutos (300 s), ítems 30-49.
 */
@Entity
@Table(name = "test_espacial")
@PrimaryKeyJoinColumn(name = "id_test")
@View(members = "nombreTest, tipoTest; tiempoLimite; tipoMovimiento; instrucciones; preguntas")
public class TestEspacial extends TestAptitud {

    /**
     * Tipo de movimiento evaluado.
     * p. ej. "DESPLAZAMIENTO", "ROTACION", "INVERSION"
     */
    @Column(name = "tipo_movimiento", nullable = false, length = 50)
    @Required
    private String tipoMovimiento;

    // ?? Constructor con valores por defecto del test ????????????

    public TestEspacial() {
        setNombreTest("Test Espacial ? Desplazamiento");
        setTipoTest("ESPACIAL");
        setTiempoLimite(300); // 5 minutos según instrucciones
        this.tipoMovimiento = "DESPLAZAMIENTO";
    }

    /**
     * Calcula el puntaje directo para el test espacial.
     * Sobreescribe la lógica base: sólo cuenta ítems 30-49.
     *
     * En el BFA Espacial, cada ítem puede tener más de una respuesta correcta
     * (p. ej. ítem 28 ? A y C).  Esta implementación delega a la superclase
     * que ya cuenta por esCorrecta=true.
     */
    @Override
    public int calcularPuntajeDirecto(java.util.List<RespuestaSujeto> respuestas) {
        return super.calcularPuntajeDirecto(respuestas);
    }

    // ?? Getters & Setters ??????????????????????????????????????

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }
}

