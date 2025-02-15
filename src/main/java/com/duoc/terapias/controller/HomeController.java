
package com.duoc.terapias.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
            model.addAttribute("roles", authentication.getAuthorities());
        } else {
            model.addAttribute("username", "No autenticado");
            model.addAttribute("roles", "Sin roles");
        }
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

