package tp.desi.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Preparacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate fecha;
    private int cantidadRaciones;
    private boolean eliminado;
    
    @ManyToOne
    private Receta receta;

    // Propiedad calculada para calorías por plato
    @Transient
    public int getCaloriasPorPlato() {
        if (receta == null) return 0;
        return receta.getIngredientes().stream()
            .mapToInt(IngredienteReceta::getCalorias)
            .sum();
    }

    // Getters y Setters
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

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }
}