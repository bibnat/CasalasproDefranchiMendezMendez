
package tp.desi.servicio;

import tp.desi.entidades.Entrega;
import tp.desi.entidades.Preparacion;

import java.time.LocalDate;
import java.util.List;

public interface EntregaService {

    void guardar(Entrega e);

    void eliminar(Long id);

    List<Entrega> listar(String filtroNombre, Long filtroNro, LocalDate filtroFecha);

    Entrega buscarPorId(Long id);

    int getRacionesEntregadasDePreparacion(Preparacion p);
}


