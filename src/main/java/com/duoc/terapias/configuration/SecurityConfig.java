
package com.duoc.terapias.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
           /* .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/css/**", "/js/**").permitAll()  // Permite acceso libre a la página home y archivos estáticos
                .requestMatchers("/especialidades").hasAnyRole("ADMIN", "TERAPEUTA") // Solo accesible para admin o terapeuta
                .requestMatchers("/error").permitAll()
                .anyRequest().authenticated() // Todas las demás rutas requieren autenticación
            )*/
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/css/**", "/debug", "/js/**").permitAll()  // Solo permite página de inicio y estáticos
                .requestMatchers("/especialidades").authenticated() // Requiere login
                .requestMatchers("/especialidades/**").hasRole("ADMIN") // Solo Admin puede modificar
                .anyRequest().authenticated() // TODO lo demás requiere autenticación
    )
            .formLogin(login -> login
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/") 
                .permitAll()
            );
        

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // No aplica encriptación
    }
}


