package tp.desi.servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tp.desi.entidades.Receta;
import tp.desi.accesoDatos.IRecetaRepo;
import tp.desi.accesoDatos.IIngredienteRepo;
import tp.desi.entidades.Ingrediente;
import tp.desi.entidades.IngredienteReceta;

@Service
public class RecetaServiceImpl implements RecetaService {

    @Autowired
    private IRecetaRepo recetaRepo;

    @Autowired
    private IIngredienteRepo ingredienteRepo;

    @Override
    public Receta guardar(Receta receta) {
        Optional<Receta> existente = recetaRepo.findByNombre(receta.getNombre());
        if (receta.getId() == null && existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe una receta con ese nombre");
        }

        receta.setEliminado(false);

        // Evita el NullPointer
        if (receta.getIngredientes() == null) {
            receta.setIngredientes(new ArrayList<>());
        }

        // Preprocesar ingredientes
        List<IngredienteReceta> procesados = new ArrayList<>();
        for (IngredienteReceta ir : receta.getIngredientes()) {
            if (ir.getIngrediente() != null && ir.getIngrediente().getId() != null) {
                Ingrediente ingrediente = ingredienteRepo.findById(ir.getIngrediente().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Ingrediente no encontrado"));

                ir.setIngrediente(ingrediente);
                ir.setReceta(receta); // ¡esto es clave para la relación bidireccional!
                procesados.add(ir);
            }
        }

        receta.setIngredientes(procesados);

        return recetaRepo.save(receta);
    }

    @Override
    public List<Receta> listar() {
        return recetaRepo.findByEliminadoFalse();
    }

    @Override
    public Optional<Receta> buscarPorNombre(String nombre) {
        return recetaRepo.findByNombre(nombre);
    }

    @Override
    public Optional<Receta> buscarPorId(Long id) {
        return recetaRepo.findById(id);
    }

    @Override
    public void eliminar(Long id) {
        Receta receta = recetaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));
        receta.setEliminado(true);
        recetaRepo.save(receta);
    }

    @Override
    public int calcularCaloriasTotales(Long id) {
        Receta receta = recetaRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));
        return receta.getIngredientes().stream()
                .mapToInt(IngredienteReceta::getCalorias)
                .sum();
    }
    
    @Override
    public List<Receta> filtrar(String nombre, Integer caloriasMin, Integer caloriasMax) {
        return recetaRepo.findByEliminadoFalse().stream()
            .filter(r -> nombre == null || r.getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(r -> {
                int cal = r.getIngredientes().stream().mapToInt(IngredienteReceta::getCalorias).sum();
                return caloriasMin == null || cal >= caloriasMin;
            })
            .filter(r -> {
                int cal = r.getIngredientes().stream().mapToInt(IngredienteReceta::getCalorias).sum();
                return caloriasMax == null || cal <= caloriasMax;
            })
            .toList();
    }

}

