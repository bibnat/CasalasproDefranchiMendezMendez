package tp.desi.entidades;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@MappedSuperclass
@Getter @Setter
public abstract class Persona {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private Integer dni;
    private String domicilio;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String ocupacion;
}
