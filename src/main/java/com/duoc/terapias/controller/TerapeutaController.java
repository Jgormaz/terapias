package com.duoc.terapias.controller;

import com.duoc.terapias.model.Comuna;
import com.duoc.terapias.model.Region;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.service.TerapeutaService;
import com.duoc.terapias.service.ComunaService;
import com.duoc.terapias.service.RegionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TerapeutaController {

    @Autowired
    private TerapeutaService terapeutaService;

    @Autowired
    private ComunaService comunaService;
    
    @Autowired
    private RegionService regionService;

    // Endpoint para listar terapeutas con sus servicios
    @RequestMapping("/")
    public String obtenerTerapeutasConServicios(Model model) {
        model.addAttribute("terapeutas", terapeutaService.obtenerTerapeutasConServicios());
        return "terapeutas";  // Nombre de la plantilla que lista los terapeutas
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
}
