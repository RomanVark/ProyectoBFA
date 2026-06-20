package ni.edu.uam.BFA.modelo;

import javax.persistence.*;
import java.util.*;

import ni.edu.uam.BFA.servicios.SesionEvaluacion;
import org.openxava.annotations.*;

// Importaciones de Lombok
import lombok.Getter;
import lombok.Setter;

/**
 * Psicólogo que administra sesiones de evaluación.
 * Hereda correo, idUsuario y nombre de Usuario.
 */
@Entity
@Table(name = "psicologo")
@View(members = "nombre, correo; codigoColegido; especialidad; sesiones")
@Getter // Lombok: Genera los métodos get() para los atributos de esta clase
@Setter // Lombok: Genera los métodos set() para los atributos de esta clase
public class Psicologo extends Usuario {

    @Column(nullable = false, unique = true, length = 50)
    @Required
    private String codigoColegido;

    @Column(length = 100)
    private String especialidad;

    /**
     * Un psicólogo administra 0..* sesiones.
     */
    @OneToMany(mappedBy = "psicologo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @CollectionView("SesionEvaluacionList")
    private List<SesionEvaluacion> sesiones = new ArrayList<>();

}