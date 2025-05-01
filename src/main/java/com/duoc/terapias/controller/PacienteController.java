
package com.duoc.terapias.controller;

import com.duoc.terapias.model.Paciente;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.service.ReservaService;
import com.duoc.terapias.service.TerapeutaService;
import com.duoc.terapias.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

    private final ReservaService reservaService;

    public PacienteController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TerapeutaService terapeutaService;

    @GetMapping("/por-terapeuta")
    public String verPacientesPorTerapeuta(Model model) {
        
        String rol = userService.getRolUsuarioActual();
        if ("ROLE_TERAPEUTA".equals(rol)) {
            String userName = userService.getUsernameUsuarioActual();
            Terapeuta terapeutaActual = terapeutaService.obtenerPorUsername(userName);
            if (terapeutaActual != null) {
                String idTerapeutaActual = terapeutaActual.getIdTerapeuta();
                List<Paciente> pacientes = reservaService.obtenerPacientesPorTerapeuta(idTerapeutaActual);
                model.addAttribute("pacientes", pacientes);

           }
       }
        
        return "pacientes-lista"; 
    }
    

}
