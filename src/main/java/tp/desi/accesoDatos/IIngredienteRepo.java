package tp.desi.accesoDatos;

import tp.desi.entidades.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IIngredienteRepo extends JpaRepository<Ingrediente, Long> {
    Ingrediente findByNombre(String nombre);
}