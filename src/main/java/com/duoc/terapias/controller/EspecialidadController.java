

package com.duoc.terapias.controller;

import com.duoc.terapias.model.Especialidad;
import com.duoc.terapias.service.EspecialidadService;
import com.duoc.terapias.service.ServicioService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/especialidades")
public class EspecialidadController {
    
    @Autowired
    private EspecialidadService especialidadService;
    
    @Autowired
    private ServicioService servicioService;
    
    @GetMapping
    public String listarEspecialidades(Model model) {
        List<Especialidad> especialidades = especialidadService.obtenerTodas();
        model.addAttribute("especialidades", especialidades);
        model.addAttribute("especialidad", new Especialidad()); // Objeto vacío para el formulario
        return "especialidades";
    }
    
    @GetMapping("/nueva")
    public String nuevaEspecialidad(Model model) {
        model.addAttribute("especialidad", new Especialidad());
        return "especialidad_form"; // Vista para crear especialidad con servicios
    }
    
    @PostMapping("/guardar")
    public String guardarEspecialidad(@ModelAttribute Especialidad especialidad, Model model) {
        if (especialidad.getIdEspecialidad() == null || especialidad.getIdEspecialidad().isEmpty()) {
            especialidad.setIdEspecialidad(generarIdEspecialidad());
        }

        Especialidad especialidadGuardada = especialidadService.guardar(especialidad);

        List<Especialidad> especialidades = especialidadService.obtenerTodas();
        model.addAttribute("especialidades", especialidades);
        model.addAttribute("especialidad", new Especialidad()); // objeto vacío para nuevo registro
        model.addAttribute("especialidadGuardada", especialidadGuardada); // para identificar la recién creada

        return "especialidades"; // no redirige, muestra la vista con datos ya cargados
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarEspecialidad(@PathVariable String id) {
        especialidadService.eliminar(id);
        return "redirect:/especialidades";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable String id, Model model) {
        Optional<Especialidad> especialidad = especialidadService.obtenerPorId(id);
        if (especialidad.isPresent()) {
            model.addAttribute("especialidad", especialidad.get());
            return "editar-especialidad"; // Vista para editar especialidad
        }
        return "redirect:/especialidades";
    }
    
    @PostMapping("/actualizar")
    public String actualizarEspecialidad(@ModelAttribute Especialidad especialidad) {
        if (especialidad.getServicios() == null) {
            especialidad.setServicios(new ArrayList<>());
        } else {
            // Filtra servicios válidos (evita insertar servicios sin ID)
            especialidad.setServicios(
                especialidad.getServicios().stream()
                    .filter(servicio -> servicio.getIdServicio() != null && !servicio.getIdServicio().isEmpty())
                    .collect(Collectors.toList())
            );
        }

        especialidadService.guardar(especialidad);
        return "redirect:/especialidades";
    }
    
    public static String generarIdEspecialidad() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String fechaFormateada = sdf.format(new Date());
        return "ES" + fechaFormateada;
    }
    
    public static String generarIdServicio() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        String fechaFormateada = sdf.format(new Date());
        return "SE" + fechaFormateada;
    }

    @PostMapping("/agregarServicio")
    public String agregarServicio(@RequestParam String idEspecialidad,
                                  @RequestParam String nombre,
                                  @RequestParam String descripcion) {
        especialidadService.agregarServicioAEspecialidad(idEspecialidad, generarIdServicio(), nombre, descripcion);
        return "redirect:/especialidades";
    }
    
    @PostMapping("/eliminarServicio")
    public String eliminarServicio(@RequestParam String idServicio, @RequestParam String idEspecialidad) {
        servicioService.eliminarPorId(idServicio);
        return "redirect:/especialidades/editar/" + idEspecialidad;
    }



}
