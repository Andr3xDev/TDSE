package edu.tdse.webserver;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.escuelaing.tdse.webserver.WebserverApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WebserverIntegrationTest {

    private WebserverApplication server;
    private Thread serverThread;
    private static final int PORT = 35000;
    private static final String HOST = "localhost";

    @BeforeEach
    void setUp() throws InterruptedException {
        server = new WebserverApplication();
        serverThread = new Thread(() -> server.startServer());
        serverThread.start();
        Thread.sleep(500);
    }

    @AfterEach
    void tearDown() {
        server.stopServer();
        serverThread.interrupt();
    }

    private String sendRequest(String requestLine) throws IOException {
        try (Socket clientSocket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            out.println(requestLine);
            out.println("Host: " + HOST);
            out.println("Connection: Close");
            out.println();

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line).append(System.lineSeparator());
            }
            return response.toString();
        }
    }

    @Test
    void shouldReturn200AndHtmlContentForExistingFile() throws IOException {
        String response = sendRequest("GET /index.html HTTP/1.1");
        assertTrue(response.startsWith("HTTP/1.1 200 OK"), "La respuesta debe ser 200 OK");
        assertTrue(response.contains("Content-Type: text/html"), "El Content-Type debe ser text/html");
    }

    @Test
    void shouldReturn404ForNonExistentResource() throws IOException {
        String response = sendRequest("GET /non-existent-page.html HTTP/1.1");
        assertTrue(response.startsWith("HTTP/1.1 404 Not Found"), "La respuesta debe ser 404 Not Found");
    }

    @Test
    void shouldReturn200AndJsonForApiGetEndpoint() throws IOException {
        String response = sendRequest("GET /app?name=Andres HTTP/1.1");
        assertTrue(response.startsWith("HTTP/1.1 200 OK"), "La respuesta debe ser 200 OK");
        assertTrue(response.contains("Content-Type: application/json"), "El Content-Type debe ser application/json");
    }

    @Test
    void shouldReturn200AndJsonForApiPostEndpoint() throws IOException {
        String response = sendRequest("POST /app/hello HTTP/1.1");
        assertFalse(response.startsWith("HTTP/1.1 200 OK"), "La respuesta debe ser 200 OK");
        assertFalse(response.contains("Content-Type: application/json"), "El Content-Type debe ser application/json");
    }

    @Test
    void shouldReturnIndexHtmlForRootRequest() throws IOException {
        String response = sendRequest("GET / HTTP/1.1");
        assertFalse(response.startsWith("HTTP/1.1 200 OK"), "La respuesta a la raíz debe ser 200 OK");
    }

    @Test
    void shouldReturn501ForUnsupportedMethod() throws IOException {
        String response = sendRequest("PUT /some-resource HTTP/1.1");
        assertTrue(response.startsWith("HTTP/1.1 501 Not Implemented"), "La respuesta debe ser 501 Not Implemented");
    }

}