package tp.desi.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    private int cantidadRaciones;

    private boolean eliminado;

    @ManyToOne
    @JoinColumn(name = "familia_id")
    private Familia familia;

    @ManyToOne
    @JoinColumn(name = "preparacion_id")
    private Preparacion preparacion;

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public int getCantidadRaciones() {
        return cantidadRaciones;
    }

    public void setCantidadRaciones(int cantidadRaciones) {
        this.cantidadRaciones = cantidadRaciones;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public Familia getFamilia() {
        return familia;
    }

    public void setFamilia(Familia familia) {
        this.familia = familia;
    }

    public Preparacion getPreparacion() {
        return preparacion;
    }

    public void setPreparacion(Preparacion preparacion) {
        this.preparacion = preparacion;
    }
}
