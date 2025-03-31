
package com.duoc.terapias.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // Obtener los roles del usuario autenticado
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            // Redirección según el rol
            if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/");
                return;
            } else if (role.equals("ROLE_TERAPEUTA")) {
                response.sendRedirect("/terapeuta/asociar-servicios");
                return;
            }
        }

        // Si no tiene un rol específico, redirigir a una página por defecto
        response.sendRedirect("/");
    }
}

