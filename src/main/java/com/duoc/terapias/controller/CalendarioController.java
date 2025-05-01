package com.duoc.terapias.controller;

import com.duoc.terapias.dto.CalendarioDTO;
import com.duoc.terapias.model.Servicio;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.service.CalendarioService;
import com.duoc.terapias.service.ServicioService;
import com.duoc.terapias.service.TerapeutaService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/calendario")
public class CalendarioController {

    @Autowired
    private TerapeutaService terapeutaService;

    @Autowired
    private CalendarioService calendarioService;
    
    @Autowired
    private ServicioService servicioService;

    @GetMapping("/terapeutas/{idTerapeuta}/servicios/{idServicio}")
    public String verCalendarioTerapeutaServicio(@PathVariable("idTerapeuta") String idTerapeuta,
                                                  @PathVariable("idServicio") String idServicio,
                                                  Model model) {
        // Obtener el terapeuta
        Terapeuta terapeuta = terapeutaService.obtenerPorId(idTerapeuta);
        if (terapeuta == null) {
            // Manejar error: terapeuta no encontrado
            return "redirect:/terapeutas";
        }

        Optional<Servicio> servicioOptional = servicioService.obtenerServicioPorId(idServicio);
        if (!servicioOptional.isPresent()) {
            // Manejar error: servicio no encontrado
            return "redirect:/terapeutas";
        }
        Servicio servicio = servicioOptional.get();

        // Obtener el calendario con semanas, días y bloques como DTO
        CalendarioDTO calendarioDTO = calendarioService.obtenerCalendarioParaTerapeutaYServicio(terapeuta, servicio);

        // Pasar datos al modelo
        model.addAttribute("terapeuta", terapeuta);
        model.addAttribute("servicio", servicio);
        model.addAttribute("semanas", calendarioDTO.getSemanas());

        return "calendario-terapeuta";
    }


}

