
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
        return "login";
    }
}



