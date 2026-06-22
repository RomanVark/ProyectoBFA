package ni.edu.uam.BFA.modelo;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import org.openxava.annotations.*;

@MappedSuperclass
@View(name = "Simple", members = "nombre, correo")
@Getter
@Setter
public abstract class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    @Hidden
    private Integer idUsuario;

    @Column(nullable = false, length = 100)
    @Required
    private String nombre;

    @Column(nullable = false, unique = true, length = 150)
    @Required
    private String correo;

}