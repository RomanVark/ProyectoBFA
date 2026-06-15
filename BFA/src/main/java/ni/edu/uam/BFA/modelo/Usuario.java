package ni.edu.uam.BFA.modelo;

import lombok.*;
import javax.persistence.*;
import org.openxava.annotations.*;


    @MappedSuperclass
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



        public Integer getIdUsuario() { return idUsuario; }
        public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getCorreo() { return correo; }
        public void setCorreo(String correo) { this.correo = correo; }
    }

