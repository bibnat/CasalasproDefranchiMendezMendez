package tp.desi.presentacion;

import tp.desi.accesoDatos.IIngredienteRepo;
import tp.desi.entidades.Receta;
import tp.desi.servicio.RecetaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/recetas")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @Autowired
    private IIngredienteRepo ingredienteRepo;

    @GetMapping
    public String listar(Model model) {
        List<Receta> recetas = recetaService.listar();

        Map<Long, Integer> caloriasPorReceta = new HashMap<>();
        for (Receta receta : recetas) {
            caloriasPorReceta.put(receta.getId(), recetaService.calcularCaloriasTotales(receta.getId()));
        }

        model.addAttribute("recetas", recetas);
        model.addAttribute("ingredientes", ingredienteRepo.findAll());
        model.addAttribute("caloriasPorReceta", caloriasPorReceta);
        return "listarRecetas";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("receta", new Receta());
        model.addAttribute("ingredientes", ingredienteRepo.findAll());
        return "formularioReceta";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Receta receta, RedirectAttributes redirectAttributes) {
        try {
            boolean esNuevo = (receta.getId() == null);
            Receta guardada = recetaService.guardar(receta);
            redirectAttributes.addFlashAttribute("mensaje", esNuevo
                ? "Receta creada correctamente."
                : "Receta actualizada correctamente.");
            return "redirect:/recetas";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            if (receta.getId() != null) {
                return "redirect:/recetas/editar/" + receta.getId();
            } else {
                return "redirect:/recetas/nueva";
            }
        }
    }


    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Receta receta = recetaService.buscarPorId(id)
            .orElseThrow(() -> new IllegalArgumentException("Receta no encontrada"));
        model.addAttribute("receta", receta);
        model.addAttribute("ingredientes", ingredienteRepo.findAll());
        return "formularioReceta";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        recetaService.eliminar(id);
        return "redirect:/recetas";
    }
    
    @GetMapping("/buscar")
    public String filtrar(@RequestParam(required = false) String nombre,
                          @RequestParam(required = false) Integer caloriasMin,
                          @RequestParam(required = false) Integer caloriasMax,
                          Model model) {

        List<Receta> recetas = recetaService.filtrar(nombre, caloriasMin, caloriasMax);

        Map<Long, Integer> caloriasPorReceta = new HashMap<>();
        for (Receta receta : recetas) {
            caloriasPorReceta.put(receta.getId(), recetaService.calcularCaloriasTotales(receta.getId()));
        }

        model.addAttribute("recetas", recetas);
        model.addAttribute("ingredientes", ingredienteRepo.findAll());
        model.addAttribute("caloriasPorReceta", caloriasPorReceta);
        return "listarRecetas";
    }

}
