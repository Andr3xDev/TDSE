package edu.escuelaing.tdse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web configuration class that customizes Spring MVC behavior.
 * <p>
 * This configuration class implements {@link WebMvcConfigurer} to provide custom Cross-Origin
 * Resource Sharing (CORS) settings for the application.
 * </p>
 * <p>
 * The CORS configuration allows cross-origin requests from specific origins, enabling the frontend
 * application to communicate with the backend API.
 * </p>
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080", "https://andres-security.duckdns.org")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH").allowedHeaders("*")
                .allowCredentials(true);
    }

}
