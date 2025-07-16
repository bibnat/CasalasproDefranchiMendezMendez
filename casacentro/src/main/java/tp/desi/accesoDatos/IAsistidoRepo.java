package tp.desi.accesoDatos;


import tp.desi.entidades.Asistido;
import org.springframework.data.jpa.repository.JpaRepository;

//Método para verificar si existe un integrante con el mismo DNI
public interface IAsistidoRepo extends JpaRepository<Asistido, Long> {
    boolean existsByDni(int dni);
}
