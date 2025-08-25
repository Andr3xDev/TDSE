package edu.escuelaing.tdse.framework;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * Represents an HTTP response to be sent to a client.
 * <p>
 * This class encapsulates the status code, status message, headers, and body of
 * an HTTP response.
 * It provides methods to set these properties and to send the response using a
 * PrintWriter
 * </p>
 */
@Data
public class HttpResponse {

    private int statusCode;
    private String statusMessage;
    private String body;
    private Map<String, String> headers = new HashMap<>();

    public HttpResponse() {
        this.statusCode = 200;
        this.statusMessage = "OK";
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public void send(PrintWriter out) {
        out.println("HTTP/1.1 " + statusCode + " " + statusMessage);
        headers.forEach((key, value) -> out.println(key + ": " + value));
        out.println();
        if (body != null) {
            out.println(body);
        }
        out.flush();
    }

}