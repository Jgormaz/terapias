
package com.duoc.terapias.controller;

import com.duoc.terapias.service.TerapeutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.ui.Model;

@Controller
public class TerapeutaController {

    @Autowired
    private TerapeutaService terapeutaService;

    @RequestMapping("/")
    public String obtenerTerapeutasConServicios(Model model) {
        // Obtenemos la lista de terapeutas con los servicios
        model.addAttribute("terapeutas", terapeutaService.obtenerTerapeutasConServicios());
        return "terapeutas";  // Nombre de la plantilla (sin la extensión .html)
    }
}

