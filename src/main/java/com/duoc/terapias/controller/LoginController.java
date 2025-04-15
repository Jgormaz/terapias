
package com.duoc.terapias.controller;


import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class LoginController {

    @GetMapping("/login-role")
    public String login(@RequestParam Map<String,String> params, HttpSession session, Model model) {
       /* String role = params.get("role");
        System.out.println(">>> Parámetros recibidos: " + params);
        System.out.println(">>> Role desde Map: " + role);

        model.addAttribute("role", role);
        session.setAttribute("selectedRole", role);*/

        return "login";
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

