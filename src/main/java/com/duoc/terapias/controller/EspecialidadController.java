

package com.duoc.terapias.controller;

import com.duoc.terapias.model.Especialidad;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.service.EspecialidadService;
import com.duoc.terapias.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/especialidades")
public class EspecialidadController {
    
    @Autowired
    private EspecialidadService especialidadService;
    
    @Autowired
    private ServicioService servicioService;
    
    @GetMapping("/{id}/servicios")
    public String mostrarServicios(@PathVariable String id, Model model) {
        Optional<Especialidad> especialidad = especialidadService.obtenerPorId(id);
        
        if (especialidad.isPresent()) {
            model.addAttribute("especialidad", especialidad.get());
            model.addAttribute("servicios", servicioService.obtenerServiciosPorEspecialidad(id));
            model.addAttribute("nuevoServicio", new Servicio());
            return "servicios";
        } else {
            return "redirect:/especialidades";
        }
    }

    @PostMapping("/{id}/servicios/agregar")
    public String agregarServicio(@PathVariable String id, @ModelAttribute Servicio servicio) {
        Optional<Especialidad> especialidad = especialidadService.obtenerPorId(id);
        
        if (especialidad.isPresent()) {
            servicio.setEspecialidad(especialidad.get());
            servicioService.guardarServicio(servicio);
        }
        
        return "redirect:/especialidades/" + id + "/servicios";
    }

    @PostMapping("/servicios/eliminar/{id}")
    public String eliminarServicio(@PathVariable String id) {
        servicioService.eliminarServicio(id);
        return "redirect:/especialidades";
    }
    
    @GetMapping
    public String listarEspecialidades(Model model) {
        List<Especialidad> especialidades = especialidadService.obtenerTodas();
        model.addAttribute("especialidades", especialidades);
        model.addAttribute("especialidad", new Especialidad()); // Agregar un objeto vacío
        return "especialidades";
    }

    
    /*@GetMapping("/editar/{id}")
    public String editarEspecialidad(@PathVariable String id, Model model) {
        Optional<Especialidad> especialidad = especialidadService.obtenerPorId(id);
        if (especialidad.isPresent()) {
            model.addAttribute("especialidad", especialidad.get());
            return "especialidad_form";
        }
        return "redirect:/especialidades";
    }*/
    
    @PostMapping("/guardar")
    public String guardarEspecialidad(@ModelAttribute Especialidad especialidad) {
        especialidadService.guardar(especialidad);
        return "redirect:/especialidades";
    }
    
    @PostMapping("/eliminar/{id}")
    public String eliminarEspecialidad(@PathVariable String id) {
        especialidadService.eliminar(id);
        return "redirect:/especialidades";
    }
    
    // Mostrar formulario de edición con los datos cargados
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable String id, Model model) {
        Optional<Especialidad> especialidad = especialidadService.obtenerPorId(id);

        if (especialidad.isPresent()) {
            model.addAttribute("especialidad", especialidad.get());
            return "editar-especialidad"; // Carga la página de edición
        } else {
            return "redirect:/especialidades"; // Si no encuentra la especialidad, vuelve a la lista
        }
    }

    // Procesar el formulario y actualizar los datos
    @PostMapping("/actualizar")
    public String actualizarEspecialidad(@ModelAttribute Especialidad especialidad) {
        especialidadService.guardar(especialidad);
        return "redirect:/especialidades"; // Redirige a la lista de especialidades
    }
}

