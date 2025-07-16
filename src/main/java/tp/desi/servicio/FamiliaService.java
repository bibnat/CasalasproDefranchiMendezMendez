package tp.desi.servicio;

import tp.desi.entidades.Familia;
import java.util.List;

public interface FamiliaService {
    void eliminar(Long id);
    Familia guardar(Familia familia);
    List<Familia> listar();
    
    
    Familia buscarPorId(Long id);  // Buscar familia activa por ID
    
    // Método de búsqueda por nombre
    List<Familia> buscarPorNombre(String nombre);
}
