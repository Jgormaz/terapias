package com.duoc.terapias.controller;

import com.duoc.terapias.model.Comuna;
import com.duoc.terapias.model.Region;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.service.TerapeutaService;
import com.duoc.terapias.service.ComunaService;
import com.duoc.terapias.service.RegionService;
import com.duoc.terapias.service.ServicioService;
import com.duoc.terapias.service.ServicioTerapeutaService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/terapeuta")
public class TerapeutaController {

    @Autowired
    private TerapeutaService terapeutaService;

    @Autowired
    private ComunaService comunaService;
    
    @Autowired
    private RegionService regionService;
    
    
    @Autowired
    private ServicioService servicioService;

    @Autowired
    private ServicioTerapeutaService servicioTerapeutaService;
    
    @PostMapping("/actualizarEstado")
    @ResponseBody // ← AÑADE ESTO si estás usando AJAX y no quieres redirección
    public ResponseEntity<String> actualizarEstado(@RequestParam("id") String id,
                                                   @RequestParam("enabled") boolean enabled) {
        terapeutaService.actualizarEstado(id, enabled);
        return ResponseEntity.ok("Estado actualizado");
    }

    @GetMapping("/lista-terapeutas")
    public String listarTerapeutasBasico(Model model) {
        List<Terapeuta> terapeutas = terapeutaService.obtenerTodos(); // Asegúrate de que exista este método en tu service
        model.addAttribute("terapeutas", terapeutas);
        return "lista-terapeutas";
    }
    
    @GetMapping("/asociar-servicios")
    public String mostrarFormularioAsociacion(Model model, Principal principal) {
        String username = principal.getName();
        Terapeuta terapeuta = terapeutaService.obtenerPorUsername(username);
        
        model.addAttribute("terapeuta", terapeuta);
        model.addAttribute("servicios", servicioService.obtenerTodosLosServicios());
        model.addAttribute("serviciosAsociados",servicioTerapeutaService.findServiciosByUserName(username)); // Método que devuelve los servicios asociados al terapeuta

        return "asociar-servicios";  // Nombre de la vista HTML
    }

    @PostMapping("/asociar-servicios")
    public String asociarServicios(@RequestParam List<String> serviciosSeleccionados, Principal principal) {
        String username = principal.getName();
        Terapeuta terapeuta = terapeutaService.obtenerPorUsername(username);
        
        servicioTerapeutaService.asociarServiciosATerapeuta(terapeuta, serviciosSeleccionados);

        return "redirect:/terapeuta/asociar-servicios?success";  // Redirige con un mensaje de éxito
    }

    // Endpoint para listar terapeutas con sus servicios
    @RequestMapping("/")
    public String obtenerTerapeutasConServicios(Model model) {
        model.addAttribute("terapeutas", terapeutaService.obtenerTerapeutasConServicios());
        return "terapeutas";  // Nombre de la plantilla que lista los terapeutas
    }
    
        
    @GetMapping("/especialidad/{id}/terapeutas")
    public String obtenerTerapeutasPorEspecialidad(@PathVariable String id, Model model) {
        model.addAttribute("terapeutas", terapeutaService.obtenerTerapeutasPorEspecialidad(id));
        return "terapeutas";  
    }

    // Endpoint GET para mostrar el formulario de creación de un nuevo terapeuta
    @GetMapping("/terapeutas/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("terapeuta", new Terapeuta());
        // Se agregan las listas de comunas y regiones para poblar los dropdowns en la vista
        List<Comuna> comunas = comunaService.obtenerTodas();
        System.out.println("Lista de comunas: " + comunas);
        model.addAttribute("comunas", comunas);
        model.addAttribute("regiones", regionService.obtenerTodas());
        return "nuevo-terapeuta";  // Nombre de la plantilla con el formulario
    }

    // Endpoint POST para procesar el formulario y guardar el nuevo terapeuta
    // Se asume que en el formulario se envían los IDs de Comuna y Región
    @PostMapping("/terapeutas/save")
    public String guardarTerapeuta(
            @ModelAttribute("terapeuta") Terapeuta terapeuta,
            @RequestParam("comuna") String idComuna,
            @RequestParam("region") String idRegion) {

        // Se obtiene el objeto Comuna y Region a partir de sus IDs
        Comuna comuna = comunaService.buscarPorId(idComuna);
        Region region = regionService.buscarPorId(idRegion);
        terapeuta.setComuna(comuna);
        terapeuta.setRegion(region);
        
        // Se guarda el terapeuta
        terapeutaService.guardar(terapeuta);
        return "redirect:/";  // Redirige a la lista de terapeutas tras guardar
    }
    
    // Mostrar formulario de edición de terapeuta
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") String id, Model model) {
        Terapeuta terapeuta = terapeutaService.obtenerPorId(id);
        if (terapeuta == null) {
            return "redirect:/terapeuta/?error";  // o una página de error específica
        }

        model.addAttribute("terapeuta", terapeuta);
        model.addAttribute("comunas", comunaService.obtenerTodas());
        model.addAttribute("regiones", regionService.obtenerTodas());

        return "editar-terapeuta";  // Debes crear esta vista similar a nuevo-terapeuta.html
    }

    // Guardar terapeuta editado
    @PostMapping("/editar/{id}")
    public String actualizarTerapeuta(
            @PathVariable("id") String id,
            @ModelAttribute("terapeuta") Terapeuta terapeutaActualizado,
            @RequestParam("comuna") String idComuna,
            @RequestParam("region") String idRegion) {

        Comuna comuna = comunaService.buscarPorId(idComuna);
        Region region = regionService.buscarPorId(idRegion);

        terapeutaActualizado.setComuna(comuna);
        terapeutaActualizado.setRegion(region);
        terapeutaActualizado.setIdTerapeuta(id); // Asegura mantener el ID original

        terapeutaService.guardar(terapeutaActualizado);
        return "redirect:/terapeuta/lista-terapeutas";
    }

    
    @GetMapping("/eliminar/{id}")
    public String eliminarTerapeuta(@PathVariable("id") String id) {
        // Paso 1: Eliminar asociaciones
        servicioTerapeutaService.eliminarPorIdTerapeuta(id);

        // Paso 2: Eliminar terapeuta
        terapeutaService.eliminarPorId(id);

        return "redirect:/terapeuta/lista-terapeutas";
    }


}
