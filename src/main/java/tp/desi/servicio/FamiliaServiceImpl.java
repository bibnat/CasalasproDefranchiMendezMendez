package tp.desi.servicio;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tp.desi.accesoDatos.IFamiliaRepo;
import tp.desi.accesoDatos.IAsistidoRepo;
import tp.desi.entidades.Familia;
import tp.desi.entidades.Asistido;

@Service
public class FamiliaServiceImpl implements FamiliaService {

    @Autowired
    private IFamiliaRepo repo;

    @Autowired
    private IAsistidoRepo asistidoRepo;

    @Override
    public List<Familia> listar() {
        return repo.findByEliminadoFalse(); // Listar solo familias activas
    }

    @Override
    public Familia buscarPorId(Long id) {
        // Solo una implementación aquí
        return repo.findByIdAndEliminadoFalse(id)
                .orElse(null); // Si no se encuentra la familia, retornamos null
    }

    @Override
    public Familia guardar(Familia familia) {
        return repo.save(familia);
    }

    @Override
    public void eliminar(Long id) {
        Familia familia = buscarPorId(id);
        if (familia != null) {
            familia.setEliminado(true);  // Marcar como eliminada lógicamente
            repo.save(familia);
        }
    }

    // Implementación del método buscarPorNombre
    @Override
    public List<Familia> buscarPorNombre(String nombre) {
        return repo.findByNombreContainingIgnoreCaseAndEliminadoFalse(nombre);
    }

    // Método para agregar un nuevo Asistido (miembro de la familia)
    public void agregarAsistido(Asistido asistido, Long familiaId) {
        Familia familia = buscarPorId(familiaId);
        if (familia != null) {
            // Validación para asegurarse de que el DNI no esté repetido
            for (Asistido existente : familia.getIntegrantes()) {
            	if (existente.getDni() == asistido.getDni()) {
            	    throw new IllegalArgumentException("El DNI ya está registrado en esta familia.");
            	}
            }
            asistido.setFamilia(familia);
            asistido.setEliminado(false); // Asegurarse de que el asistido no esté marcado como eliminado al principio
            asistidoRepo.save(asistido);
        }
    }

    // Método para eliminar un asistido (miembro de la familia) lógicamente
    public void eliminarAsistido(Long asistidoId) {
        Asistido asistido = asistidoRepo.findById(asistidoId).orElse(null);
        if (asistido != null) {
            asistido.setEliminado(true); // Eliminación lógica del asistido
            asistidoRepo.save(asistido);
        }
    }
}
