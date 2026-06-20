package ni.edu.uam.BFA.modelo;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import org.openxava.annotations.*;

@MappedSuperclass
@View(name = "Simple", members = "nombre, correo")
@Getter // Lombok: Genera getNombre(), getCorreo(), etc.
@Setter // Lombok: Genera setNombre(), setCorreo(), etc.
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    @Required
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    @Required
    private String correo;

}