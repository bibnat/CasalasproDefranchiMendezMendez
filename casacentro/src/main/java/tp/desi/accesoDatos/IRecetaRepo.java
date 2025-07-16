package tp.desi.accesoDatos;

import tp.desi.entidades.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IRecetaRepo extends JpaRepository<Receta, Long> {
    List<Receta> findByEliminadoFalse();
    Optional<Receta> findByNombre(String nombre);
}
