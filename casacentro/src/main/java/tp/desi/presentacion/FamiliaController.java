package tp.desi.presentacion;

import tp.desi.accesoDatos.IAsistidoRepo;
import tp.desi.entidades.Asistido;
import tp.desi.entidades.Familia;
import tp.desi.servicio.FamiliaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import java.util.List;

@Controller
@RequestMapping("/familias")
public class FamiliaController {

    @Autowired
    private FamiliaService familiaService;

    @Autowired
    private IAsistidoRepo asistidoRepo;

 // Muestra el listado de familias, con opción de filtrar por nombre o ID
    
    @GetMapping
    public String listar(@RequestParam(value = "nombre", required = false) String nombre,
                         @RequestParam(value = "id", required = false) Long id,
                         Model model) {

        List<Familia> familias;

       
        if (nombre != null && !nombre.isEmpty()) {
            familias = familiaService.buscarPorNombre(nombre);
        } 
       
        else if (id != null) {
            Familia familia = familiaService.buscarPorId(id); 
            if (familia != null) {
                familias = List.of(familia);  
            } else {
                familias = List.of();  
            }
        }
        
        else {
            familias = familiaService.listar();
        }

        model.addAttribute("familias", familias);
        return "listarFam";
    }
    
    // Muestra el formulario para registrar una nueva familia
    
    @GetMapping("/nueva")
    public String nueva(Model model) {
        Familia familia = new Familia();

        
        familia.setFechaRegistro(LocalDate.now());  
        

        model.addAttribute("familia", familia);
        model.addAttribute("nuevoAsistido", new Asistido());
        return "formularioFamilia";
    }

    // Guarda una nueva familia o actualiza una existente, conservando los integrantes
    
    @PostMapping
    public String guardar(@ModelAttribute Familia familia, RedirectAttributes redirectAttributes) {
        boolean esNueva = (familia.getId() == null); // 

        
        Familia existente = familiaService.buscarPorId(familia.getId());
        if (existente != null) {
            familia.setIntegrantes(existente.getIntegrantes()); 
        }

        Familia guardada = familiaService.guardar(familia);

       
        if (esNueva) {
            redirectAttributes.addFlashAttribute("mensaje", "Familia guardada con éxito. Ahora agregá al menos un integrante.");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Cambios guardados con éxito.");
        }

        return "redirect:/familias/editar/" + guardada.getId();
    }

 // Muestra el formulario para editar una familia existente

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                         @RequestParam(name = "editarId", required = false) Long editarId,
                         Model model) {
        Familia familia = familiaService.buscarPorId(id);
        if (familia == null) {
            return "redirect:/familias";
        }

        model.addAttribute("familia", familia);
        model.addAttribute("nuevoAsistido", new Asistido());
        model.addAttribute("editarId", editarId); 
        return "formularioFamilia";
    }

    // Elimina lógicamente una familia

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        familiaService.eliminar(id);
        return "redirect:/familias";
    }
    
 // Agrega un nuevo integrante a la familia

    @PostMapping("/agregarAsistido")
    public String agregarAsistido(@ModelAttribute("nuevoAsistido") Asistido asistido,
                                  @RequestParam("familiaId") Long familiaId,
                                  RedirectAttributes redirectAttributes) {
        try {
            asistido.setEliminado(false);
            asistido.setFamilia(familiaService.buscarPorId(familiaId));
            asistidoRepo.save(asistido);

            redirectAttributes.addFlashAttribute("mensaje", "Asistido agregado correctamente.");
            return "redirect:/familias/editar/" + familiaId;

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo agregar el asistido: " + e.getMessage());
            return "redirect:/familias/editar/" + familiaId;
        }
    }
    
 // Edita los datos de un integrante existente
    
    @PostMapping("/editarAsistido")
    public String editarAsistido(@RequestParam Long asistidoId,
                                 @RequestParam Long familiaId,
                                 @RequestParam int dni,
                                 @RequestParam String nombre,
                                 @RequestParam String apellido,
                                 @RequestParam String ocupacion,
                                 @RequestParam("fechaNacimiento") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
                                 RedirectAttributes redirectAttributes) {
        try {
            Asistido asistido = asistidoRepo.findById(asistidoId).orElse(null);
            if (asistido != null) {
                asistido.setDni(dni);
                asistido.setNombre(nombre);
                asistido.setApellido(apellido);
                asistido.setOcupacion(ocupacion);
                asistido.setFechaNacimiento(fechaNacimiento);
                asistidoRepo.save(asistido);
                redirectAttributes.addFlashAttribute("mensaje", "Integrante actualizado correctamente.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al editar asistido: " + e.getMessage());
        }
        return "redirect:/familias/editar/" + familiaId;
    }

 // Elimina lógicamente a un integrante de la familia
    
    @PostMapping("/eliminarAsistido")
    public String eliminarAsistido(@RequestParam Long asistidoId,
                                   @RequestParam Long familiaId,
                                   RedirectAttributes redirectAttributes) {
        try {
            Asistido asistido = asistidoRepo.findById(asistidoId).orElse(null);
            if (asistido != null) {
                asistido.setEliminado(true);
                asistidoRepo.save(asistido);
                redirectAttributes.addFlashAttribute("mensaje", "Integrante eliminado correctamente.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar asistido: " + e.getMessage());
        }
        return "redirect:/familias/editar/" + familiaId;
    }

}
