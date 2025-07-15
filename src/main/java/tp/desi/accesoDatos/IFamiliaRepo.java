package tp.desi.accesoDatos;

import tp.desi.entidades.Familia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IFamiliaRepo extends JpaRepository<Familia, Long> {
    // Método para obtener todas las familias activas (no eliminadas)
    List<Familia> findByEliminadoFalse();

    // Método para obtener una familia por ID, solo si no está eliminada
    Optional<Familia> findByIdAndEliminadoFalse(Long id);

    // Método para buscar familias por nombre, solo si están activas (no eliminadas)
    List<Familia> findByNombreContainingIgnoreCaseAndEliminadoFalse(String nombre);
}
