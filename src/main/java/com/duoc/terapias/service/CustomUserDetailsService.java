
package com.duoc.terapias.service;

import com.duoc.terapias.model.Admin;
import com.duoc.terapias.model.Terapeuta;
import com.duoc.terapias.repository.AdminRepository;
import com.duoc.terapias.repository.TerapeutaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("Intentando autenticar usuario: " + userName);

        if ("Admin".equals(userName)) {
            Admin admin = adminRepository.findByUserName(userName)
                    .orElseThrow(() -> new UsernameNotFoundException("Admin no encontrado"));

            logger.info("Admin encontrado: {}", admin.getUserName());
            logger.info("Contraseña en BD (sin encriptar): {}", admin.getPassword());

            return User.withUsername(admin.getUserName())
                    .password(admin.getPassword()) // Contraseña en texto plano
                    .roles("ADMIN")
                    .build();
        }

        Terapeuta terapeuta = terapeutaRepository.findByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        System.out.println("Terapeuta encontrado: " + terapeuta.getUserName());

        return User.withUsername(terapeuta.getUserName())
                .password(terapeuta.getPassword()) // Contraseña en texto plano
                .roles("TERAPEUTA")
                .build();
    }
}





/*@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TerapeutaRepository terapeutaRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if ("Admin".equals(userName)) {
            return User.withUsername("Admin")
                    .password(passwordEncoder.encode("adminpassword"))
                    .roles("ADMIN")
                    .build();
        }

        return terapeutaRepository.findByUserName(userName)
                .map(terapeuta -> {
                    if (!terapeuta.isEnabled()) {
                        throw new UsernameNotFoundException("Usuario no habilitado");
                    }
                    return User.withUsername(terapeuta.getUserName())
                            .password(terapeuta.getPassword())
                            .roles("TERAPEUTA")
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}*/
