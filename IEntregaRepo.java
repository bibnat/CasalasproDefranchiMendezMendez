package tp.desi.accesoDatos;

import tp.desi.entidades.Entrega;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


public interface IEntregaRepo extends JpaRepository<Entrega, Long> {
    List<Entrega> findByEliminadoFalse();
    boolean existsByFamilia_IdAndFechaAndEliminadoFalse(Long familiaId, LocalDate fecha);
    List<Entrega> findByFechaAndEliminadoFalse(LocalDate fecha);
    List<Entrega> findByFamilia_IdAndEliminadoFalse(Long familiaId);
    List<Entrega> findByFamilia_NombreContainingIgnoreCaseAndEliminadoFalse(String nombre);
}
