package tp.desi.accesoDatos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import tp.desi.entidades.Preparacion;
import tp.desi.entidades.Receta;

public interface IPreparacionRepo extends JpaRepository<Preparacion, Long> {
	
	boolean existsByRecetaAndFechaAndEliminadoFalse(Receta receta, LocalDate fecha);
    List<Preparacion> findByEliminadoFalse();
    List<Preparacion> findByFechaAndEliminadoFalse(LocalDate fecha);

}
