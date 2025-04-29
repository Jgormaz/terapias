
package com.duoc.terapias.controller;

import com.duoc.terapias.model.Especialidad;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.service.EspecialidadService;
import com.duoc.terapias.service.TerapeutaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HomeController {
    
    @Autowired
    private EspecialidadService especialidadService;
    
    @Autowired
    private TerapeutaService terapeutaService;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("roles", authentication.getAuthorities());
        } else {
            model.addAttribute("username", "No autenticado");
            model.addAttribute("roles", "Sin roles");
        }
        List<Especialidad> especialidades = especialidadService.obtenerTodas();
        model.addAttribute("especialidades", especialidades);
        List<Terapeuta> terapeutas = terapeutaService.obtenerTodos();
        model.addAttribute("terapeutas", terapeutas);
    return "home";
}
    
        @GetMapping("/debug")
    @ResponseBody
    public String debugAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getName())) {
            return "No hay sesión activa.";
        }
        return "Usuario autenticado: " + authentication.getName() + ", Roles: " + authentication.getAuthorities();
    }
}

