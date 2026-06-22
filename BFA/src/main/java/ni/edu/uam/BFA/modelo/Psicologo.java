package ni.edu.uam.BFA.modelo;

import javax.persistence.*;
import java.util.*;

import ni.edu.uam.BFA.servicios.SesionEvaluacion;
import org.openxava.annotations.*;

// Importaciones de Lombok
import lombok.Getter;
import lombok.Setter;

/**
 * Psicologo que administra sesiones de evaluación.
 * Hereda correo, idUsuario y nombre de Usuario.
 */
@Entity
@Table(name = "psicologo")
@View(members = "nombre, correo; codigoColegido; especialidad; nombreUsuario; sesiones")
@Getter // Lombok: Genera los métodos get() para los atributos de esta clase
@Setter // Lombok: Genera los métodos set() para los atributos de esta clase
public class Psicologo extends Usuario {

    @Column(name = "codigo_colegiado", nullable = false, unique = true, length = 50)
    @Required
    private String codigoColegido;

    @Column(length = 100)
    private String especialidad;

    /** Nombre de usuario único para iniciar sesión en el módulo del Test Espacial. */
    @Column(name = "nombre_usuario", unique = true, length = 50)
    private String nombreUsuario;

    /**
     * Hash de la contraseña (bcrypt), generado y validado desde el
     * servidor Node.js del Test Espacial. OpenXava nunca debe mostrar
     * este campo en pantalla ? se oculta con @Hidden.
     */
    @Column(name = "password_hash", length = 255)
    @Hidden
    private String passwordHash;

    /**
     * Un psicologo administra 0..* sesiones.
     */
    @OneToMany(mappedBy = "psicologo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SesionEvaluacion> sesiones = new ArrayList<>();

}