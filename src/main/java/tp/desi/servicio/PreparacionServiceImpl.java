package tp.desi.servicio;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tp.desi.accesoDatos.IPreparacionRepo;
import tp.desi.accesoDatos.IRecetaRepo;
import tp.desi.entidades.Ingrediente;
import tp.desi.entidades.IngredienteReceta;
import tp.desi.entidades.Preparacion;
import tp.desi.entidades.Receta;

@Service
public class PreparacionServiceImpl implements PreparacionService {

    @Autowired
    private IPreparacionRepo preparacionRepo;

    @Autowired
    private IRecetaRepo recetaRepo;

    @Override
    public void guardar(Preparacion preparacion) {
        if (preparacion.getFecha() == null || preparacion.getFecha().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura");
        }

        if (preparacion.getCantidadRaciones() <= 0) {
            throw new IllegalArgumentException("La cantidad de raciones debe ser mayor a cero");
        }

        Receta receta = recetaRepo.findById(preparacion.getReceta().getId())
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));

        if (preparacion.getId() == null && 
            preparacionRepo.existsByRecetaAndFechaAndEliminadoFalse(receta, preparacion.getFecha())) {
            throw new IllegalArgumentException("Ya existe una preparación para esa receta en esa fecha");
        }

        for (IngredienteReceta ir : receta.getIngredientes()) {
            double requerido = ir.getCantidad() * preparacion.getCantidadRaciones();
            Ingrediente ing = ir.getIngrediente();
            if (ing.getStock() < requerido) {
                throw new IllegalArgumentException("No hay stock suficiente para el ingrediente: " + ing.getNombre());
            }
        }

        for (IngredienteReceta ir : receta.getIngredientes()) {
            double requerido = ir.getCantidad() * preparacion.getCantidadRaciones();
            Ingrediente ing = ir.getIngrediente();
            ing.setStock(ing.getStock() - requerido);
        }

        preparacion.setEliminado(false);
        preparacionRepo.save(preparacion);
    }

    @Override
    public void eliminar(Long id) {
        Preparacion p = preparacionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Preparación no encontrada"));
        p.setEliminado(true);
        preparacionRepo.save(p);
    }

    @Override
    public List<Preparacion> listar() {
        return preparacionRepo.findByEliminadoFalse();
    }

    @Override
    public Preparacion buscarPorId(Long id) {
        return preparacionRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Preparación no encontrada"));
    }
    
    @Override
    public List<Preparacion> filtrar(String nombre, LocalDate fecha, Integer caloriasMin, Integer caloriasMax) {
        return preparacionRepo.findByEliminadoFalse().stream()
            .filter(p -> nombre == null || p.getReceta().getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(p -> fecha == null || p.getFecha().isEqual(fecha))
            .filter(p -> caloriasMin == null || p.getCaloriasPorPlato() >= caloriasMin)
            .filter(p -> caloriasMax == null || p.getCaloriasPorPlato() <= caloriasMax)
            .toList();
    }

    
    
    @Override
    public List<Preparacion> listarHoy() {
        LocalDate hoy = LocalDate.now();
        return preparacionRepo.findByFechaAndEliminadoFalse(hoy);
    }


    
}

