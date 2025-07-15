package tp.desi.servicio;

import tp.desi.entidades.Familia;
import java.util.List;

public interface FamiliaService {
    void eliminar(Long id);
    Familia guardar(Familia familia);
    List<Familia> listar();
    
    // Sólo dejamos una declaración de buscarPorId
    Familia buscarPorId(Long id);  // Eliminar duplicados
    
    // Método de búsqueda por nombre
    List<Familia> buscarPorNombre(String nombre);  // Buscar por nombre
}
