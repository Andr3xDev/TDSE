package edu.escuelaing.tdse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Boot application.
 * <p>
 * This class bootstraps the application using {@link SpringApplication}.
 * The {@code @SpringBootApplication} annotation enables auto-configuration,
 * component scanning, and configuration properties support.
 * </p>
 */
@SpringBootApplication
public class TdseApplication {

    public static void main(String[] args) {
        SpringApplication.run(TdseApplication.class, args);
    }

}