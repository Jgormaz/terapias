
package com.duoc.terapias.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/reservas/enviar-codigo").permitAll()
                .requestMatchers("/","/evaluaciones/**","/calendario/**","/reservas/**","/terapeuta/especialidad/**","/css/**", "/debug", "/js/**").permitAll()  // Solo permite página de inicio y estáticos
                .requestMatchers("/especialidades").authenticated() // Requiere login
                .requestMatchers("/especialidades/**").hasRole("ADMIN") // Solo Admin puede modificar
                .requestMatchers("/terapeuta/terapeutas/nuevo", "/terapeuta/terapeutas/save").hasRole("ADMIN") // Solo Admin puede modificar
                .anyRequest().authenticated() // TODO lo demás requiere autenticación
    )
            .formLogin(login -> login
                .loginPage("/login-role")                // <-- CAMBIA la ruta de login
                .loginProcessingUrl("/perform_login")      // <-- Donde se envía el POST del formulario
                .successHandler(successHandler)
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


