package edu.escuelaing.twitter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll() // Permite todos
                                                                                   // los requests
                                                                                   // sin auth
        ).csrf(csrf -> csrf.disable()) // Deshabilita CSRF para pruebas (no uses en prod)
                .httpBasic(httpBasic -> httpBasic.disable()) // Opcional: deshabilita Basic Auth
                .formLogin(formLogin -> formLogin.disable()); // Opcional: deshabilita login form
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOriginPatterns("*") // Permite todos los origenes
                                                                      // (más flexible que
                                                                      // allowedOrigins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                        .allowedHeaders("*") // Permite todos los headers incluyendo Authorization
                        .allowCredentials(false) // Importante: false para permitir patrones
                                                 // wildcard
                        .maxAge(3600); // Cache preflight por 1 hora
            }
        };
    }
}
