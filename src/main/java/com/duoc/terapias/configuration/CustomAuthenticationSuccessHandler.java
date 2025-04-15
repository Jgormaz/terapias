
package com.duoc.terapias.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

   /* @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            // Redirección según el rol
            if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/");
                return;
            } else if (role.equals("ROLE_TERAPEUTA")) {
                response.sendRedirect("/");
                return;
            }
        }

        // Si no tiene un rol específico, redirigir a una página por defecto
        response.sendRedirect("/");
    }*/
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Obtener rol seleccionado desde sesión
        HttpSession session = request.getSession(false);
        String selectedRole = (session != null) ? (String) session.getAttribute("selectedRole") : null;
        System.out.println("Rol seleccionado desde sesión: " + selectedRole);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            // Verifica también el rol "real" del usuario por si necesitas lógica adicional
            System.out.println("Rol del usuario autenticado: " + role);

            if ("ROLE_ADMIN".equals(role)) {
                response.sendRedirect("/");
                return;
            } else if ("ROLE_TERAPEUTA".equals(role)) {
                response.sendRedirect("/");
                return;
            }
        }

        response.sendRedirect("/");
    }
}

