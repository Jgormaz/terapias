
package com.duoc.terapias.controller;

import com.duoc.terapias.dto.EvaluacionDTO;
import com.duoc.terapias.service.ReservaService;
import com.duoc.terapias.service.TerapeutaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/evaluaciones")
public class EvaluacionController {

    private final ReservaService reservaService;
    private final TerapeutaService terapeutaService;

    @Autowired
    public EvaluacionController(ReservaService reservaService, TerapeutaService terapeutaService) {
        this.reservaService = reservaService;
        this.terapeutaService = terapeutaService;
    }

    @GetMapping("/nueva")
    public String mostrarFormularioEvaluacion(Model model) {
        model.addAttribute("evaluacionDTO", new EvaluacionDTO());
        return "evaluacion-formulario"; // vista del formulario
    }

    @PostMapping("/guardar")
    public String guardarEvaluacion(@ModelAttribute EvaluacionDTO evaluacionDTO, Model model) {
        try {
            reservaService.evaluarReserva(evaluacionDTO.getIdReserva(), evaluacionDTO.getNota());
            model.addAttribute("mensaje", "¡Gracias por tu evaluación!");
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "evaluacion-formulario";
    }
}

