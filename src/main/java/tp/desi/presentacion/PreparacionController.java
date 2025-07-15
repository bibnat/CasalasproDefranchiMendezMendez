package tp.desi.presentacion;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tp.desi.entidades.Preparacion;
import tp.desi.servicio.PreparacionService;
import tp.desi.servicio.RecetaService;

@Controller
@RequestMapping("/preparaciones")
public class PreparacionController {

    @Autowired
    private PreparacionService preparacionService;

    @Autowired
    private RecetaService recetaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("preparaciones", preparacionService.listar());
        return "listarPreparaciones";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("preparacion", new Preparacion());
        model.addAttribute("recetas", recetaService.listar());
        return "formularioPreparacion";
    }

    @PostMapping
    public String guardar(@ModelAttribute Preparacion preparacion, RedirectAttributes redirect) {
        try {
            preparacionService.guardar(preparacion);
            redirect.addFlashAttribute("mensaje", "Preparación registrada con éxito");
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/preparaciones/nueva";
        }

        return "redirect:/preparaciones";
    }
    
    
    @GetMapping("/buscar")
    public String filtrar(@RequestParam(required = false) String nombre,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                          @RequestParam(required = false) Integer caloriasMin,
                          @RequestParam(required = false) Integer caloriasMax,
                          Model model) {

        List<Preparacion> preparaciones = preparacionService.filtrar(nombre, fecha, caloriasMin, caloriasMax);
        model.addAttribute("preparaciones", preparaciones);
        return "listarPreparaciones";
    }



    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Preparacion p = preparacionService.buscarPorId(id);
        model.addAttribute("preparacion", p);
        return "formularioPreparacionSoloFecha"; // solo editable la fecha
    }

    @PostMapping("/editar/{id}")
    public String actualizarFecha(@PathVariable Long id, @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        Preparacion p = preparacionService.buscarPorId(id);
        p.setFecha(fecha);
        preparacionService.guardar(p);
        return "redirect:/preparaciones";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        preparacionService.eliminar(id);
        return "redirect:/preparaciones";
    }
}
