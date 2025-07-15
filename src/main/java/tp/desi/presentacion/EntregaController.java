package tp.desi.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import tp.desi.entidades.Entrega;
import tp.desi.entidades.Preparacion;
import tp.desi.servicio.EntregaService;
import tp.desi.servicio.FamiliaService;
import tp.desi.servicio.PreparacionService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/entregas")
public class EntregaController {
    @Autowired private EntregaService svc;
    @Autowired private FamiliaService famSvc;
    @Autowired private PreparacionService prepSvc;

    @GetMapping
    public String listar(@RequestParam(required=false) String nombre,
                         @RequestParam(required=false) Long nro,
                         @RequestParam(required=false) @DateTimeFormat( iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                         Model m) {
        m.addAttribute("entregas", svc.listar(nombre, nro, fecha));
        return "listarEntregas";
    }

    @GetMapping("/nueva")
    public String nueva(Model m) {
        List<Preparacion> preparacionesHoy = prepSvc.listarHoy();

        Map<Long, Integer> racionesDisponibles = new HashMap<>();
        for (Preparacion p : preparacionesHoy) {
            int entregadas = svc.getRacionesEntregadasDePreparacion(p);
            int disponibles = p.getCantidadRaciones() - entregadas;
            racionesDisponibles.put(p.getId(), disponibles);
        }

        m.addAttribute("entrega", new Entrega());
        m.addAttribute("familias", famSvc.listar());
        m.addAttribute("preparacionesHoy", preparacionesHoy);
        m.addAttribute("racionesDisponibles", racionesDisponibles); // 💡 agregado
        return "formularioEntrega";
    }


    @PostMapping
    public String guardar(@ModelAttribute Entrega e, RedirectAttributes r) {
        try {
            svc.guardar(e);
            r.addFlashAttribute("mensaje", "Entrega registrada");
            return "redirect:/entregas";
        } catch (Exception ex) {
            r.addFlashAttribute("error", ex.getMessage());
            return "redirect:/entregas/nueva";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes r) {
        svc.eliminar(id);
        r.addFlashAttribute("mensaje", "Entrega eliminada");
        return "redirect:/entregas";
    }
    
    
}
