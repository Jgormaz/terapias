
package com.duoc.terapias.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    //@Autowired
    //private UserRepository userRepository;  // Asumimos que tienes un repositorio de usuarios

    // Obtener el rol del usuario actual (podría depender de tu implementación de seguridad)
    public String getRolUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                return authority.getAuthority();  // Retorna el rol (ej. "ADMIN" o "TERAPEUTA")
            }
        }
        return null;
    }

    // Obtener el nombre de usuario actual (asumimos que el usuario logueado usa su correo o username)
    public String getUsernameUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            return auth.getName();  // Retorna el nombre de usuario (ej. correo)
        }
        return null;
    }
}

