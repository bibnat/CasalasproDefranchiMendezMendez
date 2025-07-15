package tp.desi.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Familia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private LocalDate fechaRegistro = LocalDate.now();

    private LocalDate fechaUltimaAsistencia;

    private boolean eliminado;
    
    // Fecha de alta, valor por defecto: fecha actual
    private LocalDate fechaAlta = LocalDate.now();

    @OneToMany(mappedBy = "familia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asistido> integrantes = new ArrayList<>();

    // Constructors
    public Familia() {}

    public Familia(String nombre, LocalDate fechaRegistro, boolean eliminado) {
        this.nombre = nombre;
        this.fechaRegistro = fechaRegistro;
        this.eliminado = eliminado;
    }

    // Getters & Setters

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public LocalDate getFechaUltimaAsistencia() {
        return fechaUltimaAsistencia;
    }

    public void setFechaUltimaAsistencia(LocalDate fechaUltimaAsistencia) {
        this.fechaUltimaAsistencia = fechaUltimaAsistencia;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public List<Asistido> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Asistido> integrantes) {
        this.integrantes = integrantes;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    // Método para obtener la cantidad de integrantes activos no eliminados
    public int getCantidadIntegrantesActivos() {
        return (int) integrantes.stream()
                .filter(a -> !a.isEliminado())
                .count();
    }
}
