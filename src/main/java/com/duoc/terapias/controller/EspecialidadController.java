

package com.duoc.terapias.controller;

import com.duoc.terapias.model.Especialidad;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.service.EspecialidadService;
import com.duoc.terapias.service.ServicioService;
import java.util.ArrayList;
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
    public String guardarEspecialidad(@ModelAttribute Especialidad especialidad,
                                      @RequestParam("idsServicios") List<String> idsServicios,
                                      @RequestParam("nombresServicios") List<String> nombresServicios,
                                      @RequestParam("descripcionesServicios") List<String> descripcionesServicios) {
        List<Servicio> servicios = new ArrayList<>();

        for (int i = 0; i < idsServicios.size(); i++) {
            Servicio servicio = new Servicio();
            servicio.setIdServicio(idsServicios.get(i)); // Se asigna el ID ingresado manualmente
            servicio.setNombre(nombresServicios.get(i));
            servicio.setDescripcion(descripcionesServicios.get(i));
            servicio.setEspecialidad(especialidad);
            servicios.add(servicio);
        }

        especialidad.setServicios(servicios);
        especialidadService.guardar(especialidad);

        return "redirect:/especialidades";
    }
    
    @PostMapping("/eliminar/{id}")
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
        }
        especialidadService.guardar(especialidad);
        return "redirect:/especialidades";
    }
}
