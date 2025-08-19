package edu.escuelaing.tdse.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.logging.Logger;

/**
 * The {@code WebserverApplication} class implements a simple HTTP web server.
 * <p>
 * It listens for incoming client connections on a specified port and handles
 * each connection. The server can be started and stopped programmatically.
 * </p>
 */
public class WebserverApplication {

    // Loger from Lombok
    private static final Logger logger = Logger.getLogger(WebserverApplication.class.getName());

    // Server variables
    private static final int PORT = 35000;
    private ServerSocket serverSocket;
    private boolean running = true;

    public static void main(String[] args) throws IOException, URISyntaxException {
        WebserverApplication server = new WebserverApplication();
        server.startServer();
    }

    public void startServer() {
        try {
            this.serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            logger.severe("Failed to start server on port: " + PORT);
            System.exit(1);
        }

        while (running) {
            try {
                logger.info("Server started on port: " + PORT);
                Socket clientSocket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(clientSocket);
                requestHandler.handleRequest();
            } catch (IOException e) {
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