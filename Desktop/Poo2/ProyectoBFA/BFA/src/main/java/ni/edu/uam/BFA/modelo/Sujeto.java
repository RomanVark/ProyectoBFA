package ni.edu.uam.BFA.modelo;



import javax.persistence.*;
import java.util.*;

import ni.edu.uam.BFA.servicios.SesionEvaluacion;
import org.openxava.annotations.*;

// Importaciones de Lombok
import lombok.Getter;
import lombok.Setter;

/**
 * Sujeto evaluado (paciente).
 * Hereda correo, idUsuario y nombre de Usuario.
 */
@Entity
@Table(name = "sujeto")
@View(members = "nombre, correo; edad, genero; profesion; "
        + "estudiosRealizados; fechaNacimiento; sesiones")
@Getter // Lombok: Genera los métodos get()
@Setter // Lombok: Genera los métodos set()
public class Sujeto extends Usuario {

    @Column
    private Integer edad;

    @Column(nullable = false, length = 20)
    @Required
    private String genero;

    @Column(length = 100)
    private String profesion;

    @Column(length = 200)
    private String estudiosRealizados;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    /** Un sujeto puede tener múltiples sesiones de evaluación. */
    @OneToMany(mappedBy = "sujeto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SesionEvaluacion> sesiones = new ArrayList<>();

}