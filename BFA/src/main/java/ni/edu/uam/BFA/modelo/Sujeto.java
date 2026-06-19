package ni.edu.uam.BFA.modelo;

import javax.persistence.*;
import java.util.*;

import ni.edu.uam.BFA.servicios.SesionEvaluacion;
import org.openxava.annotations.*;

/**
 * Sujeto evaluado (paciente).
 * Hereda correo, idUsuario y nombre de Usuario.
 */
@Entity
@Table(name = "sujeto")
@View(members = "nombre, correo; edad, genero; profesion; "
        + "estudiosRealizados; fechaNacimiento; sesiones")
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
    @CollectionView("SesionEvaluacionList")
    private List<SesionEvaluacion> sesiones = new ArrayList<>();

    // ?? Getters & Setters ??????????????????????????????????????

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getProfesion() { return profesion; }
    public void setProfesion(String profesion) { this.profesion = profesion; }

    public String getEstudiosRealizados() { return estudiosRealizados; }
    public void setEstudiosRealizados(String estudiosRealizados) { this.estudiosRealizados = estudiosRealizados; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public List<SesionEvaluacion> getSesiones() { return sesiones; }
    public void setSesiones(List<SesionEvaluacion> sesiones) { this.sesiones = sesiones; }
}

