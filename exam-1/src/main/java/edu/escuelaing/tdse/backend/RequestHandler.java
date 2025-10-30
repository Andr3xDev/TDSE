package edu.escuelaing.tdse.backend;

import java.net.*;
import java.io.*;

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

    public void requestHandler() {
        throw new UnsupportedOperationException("Unimplemented method 'requestHandler'");
    }

    public static String notFound() {
        String body = "<!DOCTYPE html><html><head><title>404 Not Found</title></head>"
                + "<body><h1>404 Not Found</h1><p>The requested resource was not found on this server.</p></body></html>";
        return body;
    }

}