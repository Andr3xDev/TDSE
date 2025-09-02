package edu.escuelaing.tdse.framework;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The main entry point for the TDSE Framework application.
 * <p>
 * This class initializate the web server to use the mini-spring-boot framework
 * </p>
 *
 * @author Andrés Chavarro
 */
public class FrameworkApplication {

    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpServer.main(args);
    }

}