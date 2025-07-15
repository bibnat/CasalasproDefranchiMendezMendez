package tp.desi.presentacion;

import tp.desi.entidades.Asistido;
import tp.desi.entidades.Familia;
import tp.desi.accesoDatos.IAsistidoRepo;
import tp.desi.accesoDatos.IFamiliaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/asistidos")
public class AsistidoController {

    @Autowired
    private IAsistidoRepo asistidoRepo;

    @Autowired
    private IFamiliaRepo familiaRepo;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("asistidos", asistidoRepo.findAll());
        return "listarAsistidos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("asistido", new Asistido());
        model.addAttribute("familias", familiaRepo.findByEliminadoFalse());
        return "formularioAsistido";
    }

    @PostMapping
    public String guardar(@ModelAttribute Asistido asistido,
                          @RequestParam("familiaId") Long familiaId) {
        Familia familia = familiaRepo.findById(familiaId)
            .orElseThrow(() -> new IllegalArgumentException("Familia no encontrada con ID: " + familiaId));

        asistido.setFamilia(familia);
        asistido.setEliminado(false);
        asistidoRepo.save(asistido);

        return "redirect:/asistidos";
    }



    

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Optional<Asistido> asistido = asistidoRepo.findById(id);
        if (asistido.isEmpty()) {
            throw new IllegalArgumentException("ID inválido: " + id);
        }
        model.addAttribute("asistido", asistido.get());
        model.addAttribute("familias", familiaRepo.findByEliminadoFalse());
        return "formularioAsistido";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        Asistido asistido = asistidoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        asistido.setEliminado(true);
        asistidoRepo.save(asistido);
        return "redirect:/asistidos";
    }
}
