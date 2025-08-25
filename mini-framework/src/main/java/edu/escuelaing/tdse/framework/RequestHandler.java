package edu.escuelaing.tdse.framework;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.BiFunction;

/**
 * Handles HTTP requests for a simple web server.
 * <p>
 * This class processes incoming HTTP requests from a client socket, routes them
 * to the appropriate handler
 * based on the HTTP method and URI, and sends back the corresponding HTTP
 * response.
 * </p>
 */
public class RequestHandler {

    private final Socket clientSocket;
    private String ruta;
    PrintWriter out;
    BufferedReader in;
    BufferedOutputStream bodyOut;

    public RequestHandler(Socket clientSocket, String ruta) {
        this.clientSocket = clientSocket;
        this.ruta = ruta;
    }

    public void handlerRequest() throws IOException, URISyntaxException {

        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        bodyOut = new BufferedOutputStream(clientSocket.getOutputStream());

        String inputLine;
        boolean isFirstLine = true;
        String file = "";
        String method = "";
        while ((inputLine = in.readLine()) != null) {
            if (isFirstLine) {
                file = inputLine.split(" ")[1];
                method = inputLine.split(" ")[0];
                isFirstLine = false;
            }
            if (!in.ready()) {
                break;
            }
        }
        rediretMethod(method, file);
        out.close();
        bodyOut.close();
        in.close();
        clientSocket.close();

    }

    public void rediretMethod(String method, String file) throws IOException, URISyntaxException {
        URI requestFile = new URI(file);
        String fileRequest = requestFile.getPath();
        String query = requestFile.getQuery();

        HttpRequest req = new HttpRequest(query);
        HttpResponse res = new HttpResponse();
        String contentType = getContentType(fileRequest);

        if (fileRequest.startsWith("/app")) {
            BiFunction<HttpRequest, HttpResponse, String> service = null;
            String code = "404";
            String outputLine = " ";
            if (method.equals("GET")) {
                service = HttpServer.servicesGet.get(fileRequest);
                code = "200";
            } else if (method.equals("POST")) {
                service = HttpServer.servicesPost.get(fileRequest);
                code = "201";
            }

            if (service != null) {
                outputLine = service.apply(req, res);
                outputLine = "{\"response\":\"" + outputLine + "\"}";
            } else {
                outputLine = "{\"response\":Method not supported}";
                ;
                code = "404";
            }

            String responseHeader = requestHeader("text/json", outputLine.length(), code);
            out.println(responseHeader);
            out.flush();
            out.println(outputLine);
            out.flush();
        } else {
            requestStaticHandler(ruta + file, contentType);
        }
    }

    public void requestStaticHandler(String file, String contentType) throws IOException {
        if (fileExists(file)) {
            byte[] requestfile = readFileData(file);
            String requestHeader = requestHeader(contentType, requestfile.length, "200");
            out.println(requestHeader);
            out.flush();
            bodyOut.write(requestfile);
            bodyOut.flush();
        } else {
            out.println(notFound());
        }
    }

    public byte[] readFileData(String requestFile) throws IOException {
        File file = new File(requestFile);

        if (file.isDirectory()) {
            throw new FileNotFoundException("La ruta solicitada es un directorio, no un archivo: " + requestFile);
        }
        if (!fileExists(requestFile)) {
            throw new FileNotFoundException("Archivo no encontrado: " + requestFile);
        }

        int fileLength = (int) file.length();
        FileInputStream fileIn = null;
        byte[] fileData = new byte[fileLength];
        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null) {
                fileIn.close();
            }
        }
        return fileData;
    }

    public static boolean fileExists(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }

    public String getContentType(String requestFile) {
        String contentType = " ";
        if (requestFile.endsWith(".html")) {
            contentType = "text/html";
        } else if (requestFile.endsWith(".css")) {
            contentType = "text/css";
        } else if (requestFile.endsWith(".js")) {
            contentType = "application/javascript";
        } else if (requestFile.endsWith(".png")) {
            contentType = "image/png";
        } else if (requestFile.endsWith(".jpg") || requestFile.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else {
            contentType = "text/plain";
        }
        return contentType;
    }

    public String requestHeader(String contentType, int contentLength, String code) {
        String outHeader = "HTTP/1.1 " + code + " OK\r\n"
                + "Content-Type: " + contentType + "\r\n"
                + "Content-Length: " + contentLength + "\r\n";
        return outHeader;
    }

    public static String notFound() {
        String body = "<!DOCTYPE html><html><head><title>404 Not Found</title></head>"
                + "<body><h1>404 Not Found</h1><p>The requested resource was not found on this server.</p></body></html>";
        String header = "HTTP/1.1 404 Not Found\r\n"
                + "Content-type: text/html\r\n"
                + "Content-Length: " + body.getBytes().length + "\r\n"
                + "\r\n";
        return header + body;
    }

}