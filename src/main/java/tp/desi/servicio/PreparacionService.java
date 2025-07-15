package tp.desi.servicio;

import java.time.LocalDate;
import java.util.List;

import tp.desi.entidades.Preparacion;

public interface PreparacionService {
    void guardar(Preparacion preparacion);
    void eliminar(Long id);
    List<Preparacion> listar();
    List<Preparacion> filtrar(String nombre, LocalDate fecha, Integer caloriasMin, Integer caloriasMax);
    
    List<Preparacion> listarHoy();
    Preparacion buscarPorId(Long id);
}



