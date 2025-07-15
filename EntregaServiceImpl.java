package tp.desi.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.desi.entidades.Entrega;
import tp.desi.entidades.Familia;
import tp.desi.entidades.Preparacion;
import tp.desi.accesoDatos.IEntregaRepo;
import tp.desi.accesoDatos.IFamiliaRepo;
import tp.desi.accesoDatos.IPreparacionRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EntregaServiceImpl implements EntregaService {

    @Autowired private IEntregaRepo repo;
    @Autowired private IFamiliaRepo familiaRepo; 
    @Autowired private FamiliaService famSvc;
    @Autowired private PreparacionService prepSvc;

    private int racionesEntregadasDePreparacion(Preparacion preparacion) {
        return repo.findByEliminadoFalse().stream()
            .filter(e -> e.getPreparacion().getId().equals(preparacion.getId()))
            .mapToInt(Entrega::getCantidadRaciones)
            .sum();
    }

    
    @Override
    public void guardar(Entrega e) {
        e.setFecha(LocalDate.now());

        if (e.getCantidadRaciones() <= 0)
            throw new IllegalArgumentException("Raciones > 0");

        Familia f = famSvc.buscarPorId(e.getFamilia().getId());

        if (repo.existsByFamilia_IdAndFechaAndEliminadoFalse(f.getId(), e.getFecha()))
            throw new IllegalArgumentException("Esa familia ya recibió hoy");

        if (e.getCantidadRaciones() > f.getCantidadIntegrantesActivos())
            throw new IllegalArgumentException("No puede entregar más raciones que integrantes activos");

        Preparacion prep = prepSvc.buscarPorId(e.getPreparacion().getId());

        int entregadas = racionesEntregadasDePreparacion(prep);
        int disponibles = prep.getCantidadRaciones() - entregadas;

        if (e.getCantidadRaciones() > disponibles)
            throw new IllegalArgumentException("No hay suficientes raciones disponibles de: " + prep.getReceta().getNombre());

        e.setEliminado(false);
        repo.save(e);

        // 🔁 ACTUALIZAR fecha de última asistencia
        f.setFechaUltimaAsistencia(e.getFecha());
        familiaRepo.save(f);
    }


    @Override
    public void eliminar(Long id) {
        Entrega e = repo.findById(id).orElseThrow();
        if (!e.isEliminado()) {
            e.setEliminado(true);
            repo.save(e);
        }
    }

    @Override
    public List<Entrega> listar(String nombre, Long nro, LocalDate fecha) {
        List<Entrega> all = repo.findByEliminadoFalse();
        return all.stream()
            .filter(e -> nombre == null || e.getFamilia().getNombre().toLowerCase().contains(nombre.toLowerCase()))
            .filter(e -> nro == null || e.getFamilia().getId().equals(nro))
            .filter(e -> fecha == null || e.getFecha().isEqual(fecha))
            .toList();
    }

    @Override
    public Entrega buscarPorId(Long id) {
        return repo.findById(id).orElseThrow();
    }
    
    @Override
    public int getRacionesEntregadasDePreparacion(Preparacion p) {
        return (int) repo.findByEliminadoFalse().stream()
                .filter(e -> e.getPreparacion().getId().equals(p.getId()))
                .mapToInt(Entrega::getCantidadRaciones)
                .sum();
    }

}
