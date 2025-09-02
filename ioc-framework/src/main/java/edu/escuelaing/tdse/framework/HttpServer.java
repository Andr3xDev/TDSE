package edu.escuelaing.tdse.framework;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import edu.escuelaing.tdse.framework.config.FrameworkSettings;

/**
 * HttpServer is a simple HTTP server framework that allows registering RESTful
 * services using annotations to get resources easily
 * 
 * <p>
 * Features:
 * <ul>
 * <li>Serve static files from a configurable resource path using</li>
 * <li>Start and stop the server with {@link #startServer()} and
 * {@link #stopServer()}.</li>
 * </ul>
 * </p>
 */
public class HttpServer {

    // Loger from Lombok
    private static final Logger logger = Logger.getLogger(HttpServer.class.getName());

    // Server variables
    private static final int PORT = 35000;
    private ServerSocket serverSocket;
    private boolean running = true;

    // REST services
    private static String RESOURCE_PATH = "src/main/java/edu/escuelaing/tdse/framework/public";

    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpServer server = new HttpServer();
        server.startServer();
    }

    public void startServer() throws URISyntaxException {
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            logger.severe("Failed to start server on port: " + PORT);
            System.exit(1);
        }

        while (running) {
            try {
                logger.info("Server started on port: " + PORT);
                FrameworkSettings.loadComponents();
                Socket clientSocket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(clientSocket, RESOURCE_PATH);
                requestHandler.handlerRequest();
            } catch (IOException | ClassNotFoundException e) {
                if (!running) {
                    logger.info("Server stopped.");
                    break;
                }
                logger.severe("Error accepting connection: " + e.getMessage());
            }
        }
    }

    public void stopServer() {
        this.running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                logger.info("Server stopped successfully.");
            } catch (IOException e) {
                logger.severe("Error closing server: " + e.getMessage());
            }
        }
    }

}