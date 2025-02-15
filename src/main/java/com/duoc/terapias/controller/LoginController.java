
package com.duoc.terapias.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "role", required = false) String role, Model model) {
        model.addAttribute("role", role);
        return "login"; // Renderiza login.html
    }
}

/*@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login"; // Renderiza la plantilla login.html
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String role = auth.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_ADMIN")) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/terapeuta/dashboard";
        }
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-dashboard";
    }

    @GetMapping("/terapeuta/dashboard")
    public String terapeutaDashboard() {
        return "terapeuta-dashboard";
    }
}*/

