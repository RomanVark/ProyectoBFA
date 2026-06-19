package ni.edu.uam.BFA.modelo;

import javax.persistence.*;
import java.util.*;

import ni.edu.uam.BFA.servicios.SesionEvaluacion;
import org.openxava.annotations.*;

/**
 * Psicólogo que administra sesiones de evaluación.
 * Hereda correo, idUsuario y nombre de Usuario.
 */
@Entity
@Table(name = "psicologo")
@View(members = "nombre, correo; codigoColegido; especialidad; sesiones")
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

    // ?? Getters & Setters ??????????????????????????????????????

    public String getCodigoColegido() { return codigoColegido; }
    public void setCodigoColegido(String codigoColegido) { this.codigoColegido = codigoColegido; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public List<SesionEvaluacion> getSesiones() { return sesiones; }
    public void setSesiones(List<SesionEvaluacion> sesiones) { this.sesiones = sesiones; }
}