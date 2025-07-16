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
            familia.setEliminado(true);  // Eliminación lógica de la familia
            repo.save(familia);
        }
    }

  
    @Override
    public List<Familia> buscarPorNombre(String nombre) {
        return repo.findByNombreContainingIgnoreCaseAndEliminadoFalse(nombre);
    }
    
 // Metodo para agregar un integrante a la familia, validando que el DNI no se repita 
    
    public void agregarAsistido(Asistido asistido, Long familiaId) {
        Familia familia = buscarPorId(familiaId);
        if (familia != null) {
           
            for (Asistido existente : familia.getIntegrantes()) {
            	if (existente.getDni() == asistido.getDni()) {
            	    throw new IllegalArgumentException("El DNI ya está registrado en esta familia.");
            	}
            }
            asistido.setFamilia(familia);
            asistido.setEliminado(false);
            asistidoRepo.save(asistido);
        }
    }

    // Método para eliminar a un integrante lógicamente
    public void eliminarAsistido(Long asistidoId) {
        Asistido asistido = asistidoRepo.findById(asistidoId).orElse(null);
        if (asistido != null) {
            asistido.setEliminado(true);
            asistidoRepo.save(asistido);
        }
    }
}
