package tp.desi.servicio;

import tp.desi.entidades.Receta;
import java.util.List;
import java.util.Optional;

public interface RecetaService {
    Receta guardar(Receta receta);
    List<Receta> listar();
    Optional<Receta> buscarPorNombre(String nombre);
    Optional<Receta> buscarPorId(Long id);
    List<Receta> filtrar(String nombre, Integer caloriasMin, Integer caloriasMax);

    void eliminar(Long idLong);
    int calcularCaloriasTotales(Long id);
    
}



