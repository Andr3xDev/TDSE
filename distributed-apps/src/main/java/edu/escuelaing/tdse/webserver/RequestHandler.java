package edu.escuelaing.tdse.webserver;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Handles HTTP requests for a simple web server.
 * <p>
 * This class processes incoming HTTP requests from a client socket, routes them
 * to the appropriate handler
 * based on the HTTP method and URI, and sends back the corresponding HTTP
 * response.
 * </p>
 * <ul>
 * <li>Supports serving static resources from a specified resource root
 * directory.</li>
 * <li>Handles specific API endpoints for GET and POST requests.</li>
 * <li>Returns appropriate HTTP status codes for unsupported methods and missing
 * resources.</li>
 * </ul>
 */
public class RequestHandler {

    // Server paths
    private static final String RESOURCE_ROOT = "src/main/java/edu/escuelaing/tdse/webserver/resources";
    private static final String API_ENDPOINT = "/app";
    private static final String API_HELLO_ENDPOINT = "/app/hello";

    // Constructor injection
    private final Socket clientSocket;

    public RequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void handleRequest() {
        try (
                OutputStream out = clientSocket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String requestLine = in.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                return;
            }

            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) {
                return;
            }
            String method = requestParts[0];
            String uri = requestParts[1];

            System.out.println("Received: " + requestLine);

            routeRequest(method, uri, out);

        } catch (IOException e) {
            System.err.println("Error handling request: " + e.getMessage());
        }
    }

    private void routeRequest(String method, String uri, OutputStream out) throws IOException {
        String path = uri.split("\\?")[0];
        String query = uri.contains("?") ? uri.split("\\?")[1] : null;

        if ("GET".equals(method)) {
            handleGetRequest(path, query, out);
        } else if ("POST".equals(method)) {
            handlePostRequest(path, query, out);
        } else {
            sendTextResponse(out, "text/html", "<h1>501 Not Implemented</h1>", "501 Not Implemented");
        }
    }

    private void handleGetRequest(String path, String query, OutputStream out) throws IOException {
        if (path.equals(API_ENDPOINT)) {
            String response = new RestController().responseGET(query, "GET");
            sendTextResponse(out, "application/json", response, "200 OK");
            return;
        }

        Path filePath = Paths.get(RESOURCE_ROOT + path);

        if (!Files.exists(filePath) || Files.isDirectory(filePath)) {
            sendNotFoundResponse(out);
            return;
        }

        String contentType = getContentType(filePath.toString());
        byte[] fileBytes = Files.readAllBytes(filePath);
        sendBinaryResponse(out, contentType, fileBytes, "200 OK");
    }

    private void handlePostRequest(String path, String query, OutputStream out) throws IOException {
        if (path.equals(API_HELLO_ENDPOINT)) {
            String response = new RestController().responsePOST(query, "POST");
            sendTextResponse(out, "application/json", response, "200 OK");
        } else {
            sendNotFoundResponse(out);
        }
    }

    private void sendTextResponse(OutputStream out, String contentType, String body, String status) throws IOException {
        sendBinaryResponse(out, contentType, body.getBytes(StandardCharsets.UTF_8), status);
    }

    private void sendBinaryResponse(OutputStream out, String contentType, byte[] body, String status)
            throws IOException {
        String headers = "HTTP/1.1 " + status + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + body.length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";

        out.write(headers.getBytes(StandardCharsets.UTF_8));
        out.write(body);
        out.flush();
    }

    private void sendNotFoundResponse(OutputStream out) throws IOException {
        String body = "<!DOCTYPE html><html><head><title>404 Not Found</title></head>" +
                "<body><h1>404 Not Found</h1><p>The requested resource was not found on this server.</p></body></html>";
        sendTextResponse(out, "text/html", body, "404 Not Found");
    }

    private String getContentType(String filePath) {
        if (filePath.endsWith(".html"))
            return "text/html";
        if (filePath.endsWith(".css"))
            return "text/css";
        if (filePath.endsWith(".js"))
            return "application/javascript";
        if (filePath.endsWith(".png"))
            return "image/png";
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg"))
            return "image/jpeg";
        return "application/octet-stream";
    }

}