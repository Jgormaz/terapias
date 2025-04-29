
package com.duoc.terapias.controller;

import com.duoc.terapias.dto.ResumenReservaDTO;
import com.duoc.terapias.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // Método para mostrar el reporte de terapeutas
    @GetMapping("/terapeutas")
    public String obtenerReporteTerapeutas(Model model) {
        List<ResumenReservaDTO> reporte = reporteService.obtenerReporteReservas();
        model.addAttribute("reporte", reporte);
        return "reporteTerapeutas";
    }


}

