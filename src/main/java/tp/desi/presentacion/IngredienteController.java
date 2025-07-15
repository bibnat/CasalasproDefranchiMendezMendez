package tp.desi.presentacion;

import tp.desi.accesoDatos.IIngredienteRepo;
import tp.desi.entidades.Ingrediente;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ingredientes")
public class IngredienteController {

    @Autowired
    private IIngredienteRepo ingredienteRepo;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ingredientes", ingredienteRepo.findAll());
        return "listarIngredientes";
    }

    @GetMapping("/nuevo")
    public String nuevo(@RequestParam(required = false) Long recetaId, Model model) {
        model.addAttribute("ingrediente", new Ingrediente());
        model.addAttribute("recetaId", recetaId); // se pasa al formulario
        return "formularioIngrediente";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Ingrediente ingrediente,
                          @RequestParam(required = false) Long recetaId,
                          RedirectAttributes redirectAttributes) {
        ingrediente.setEliminado(false); // importante

        try {
            ingredienteRepo.save(ingrediente);
            redirectAttributes.addFlashAttribute("mensaje", "Ingrediente guardado con éxito ✔️");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el ingrediente: " + e.getMessage());
            return "redirect:/ingredientes/nuevo" + (recetaId != null ? "?recetaId=" + recetaId : "");
        }

        // 🔁 Si fue creado desde una receta, volvemos a esa edición
        if (recetaId != null) {
            return "redirect:/recetas/editar/" + recetaId;
        }

        // Si no, vamos al listado normal
        return "redirect:/ingredientes";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Ingrediente ingrediente = ingredienteRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ingrediente no encontrado"));
        model.addAttribute("ingrediente", ingrediente);
        return "formularioIngrediente";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        ingredienteRepo.deleteById(id);
        return "redirect:/ingredientes";
    }

    // ✅ Para recargar el combo dinámicamente vía JS
    @GetMapping("/opciones")
    @ResponseBody
    public String getOpcionesIngredientes() {
        List<Ingrediente> lista = ingredienteRepo.findAll();
        StringBuilder sb = new StringBuilder();
        for (Ingrediente i : lista) {
            sb.append("<option value='").append(i.getId()).append("'>")
              .append(i.getNombre()).append("</option>");
        }
        return sb.toString();
    }
}
