package edu.escuelaing.tdse.webserver;

/**
 * The {@code RestController} class provides methods to generate HTTP responses
 * for GET and POST requests. It constructs simple JSON responses based on the
 * provided query parameters.
 */
public class RestController {

    public String responseGET(String query, String method) {
        String name = query.split("=")[1];
        String response = "HTTP/1.1. 200 OK\r\n"
                + "Content-Type: text/json\r\n"
                + "\r\n"
                + "{\"name\":\"" + name + "\", \"message\":Http request type GET done}";

        return response;

    }

    public String responsePOST(String query, String method) {
        String name = query.split("=")[1];
        String response = "HTTP/1.1. 201 OK\r\n"
                + "Content-Type: text/json\r\n"
                + "\r\n"
                + "{\"name\":\"" + name + "\", \"message\":HTTP request type POST done}";

        return response;

    }

}