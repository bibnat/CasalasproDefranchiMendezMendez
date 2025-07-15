package tp.desi.accesoDatos;


import tp.desi.entidades.Asistido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAsistidoRepo extends JpaRepository<Asistido, Long> {
    boolean existsByDni(int dni);
}
