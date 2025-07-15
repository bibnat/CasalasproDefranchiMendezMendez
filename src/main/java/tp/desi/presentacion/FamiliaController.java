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

    @GetMapping
    public String listar(@RequestParam(value = "nombre", required = false) String nombre,
                         @RequestParam(value = "id", required = false) Long id,
                         Model model) {

        List<Familia> familias;

        // Si se pasa un nombre, buscar por nombre
        if (nombre != null && !nombre.isEmpty()) {
            familias = familiaService.buscarPorNombre(nombre);
        } 
        // Si se pasa un ID, buscar por ID
        else if (id != null) {
            Familia familia = familiaService.buscarPorId(id);  // Buscar una familia por ID
            if (familia != null) {
                familias = List.of(familia);  // Crear una lista con una sola familia
            } else {
                familias = List.of();  // Si no se encuentra la familia, retornamos una lista vacía
            }
        }
        // Si no se pasa nombre ni ID, mostrar todas las familias activas
        else {
            familias = familiaService.listar();
        }

        model.addAttribute("familias", familias);
        return "listarFam";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        Familia familia = new Familia();

        // Asignar la fecha actual a fechaRegistro
        familia.setFechaRegistro(LocalDate.now());  // Aquí asignamos la fecha actual al campo fechaRegistro
        

        model.addAttribute("familia", familia);
        model.addAttribute("nuevoAsistido", new Asistido());
        return "formularioFamilia";
    }

    @PostMapping
    public String guardar(@ModelAttribute Familia familia, RedirectAttributes redirectAttributes) {
        boolean esNueva = (familia.getId() == null); // Si no tiene ID, es nueva

        // Si la familia ya existe, conservar los integrantes previos
        Familia existente = familiaService.buscarPorId(familia.getId());
        if (existente != null) {
            familia.setIntegrantes(existente.getIntegrantes()); // evita que se borren
        }

        Familia guardada = familiaService.guardar(familia);

        // Mostrar mensaje según si es nueva o existente
        if (esNueva) {
            redirectAttributes.addFlashAttribute("mensaje", "Familia guardada con éxito. Ahora agregá al menos un integrante.");
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "Cambios guardados con éxito.");
        }

        return "redirect:/familias/editar/" + guardada.getId();
    }


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
        model.addAttribute("editarId", editarId); // se pasa al HTML para habilitar ese integrante
        return "formularioFamilia";
    }


    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        familiaService.eliminar(id);
        return "redirect:/familias";
    }

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
