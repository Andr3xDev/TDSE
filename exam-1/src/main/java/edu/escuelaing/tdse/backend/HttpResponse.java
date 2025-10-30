package edu.escuelaing.tdse.backend;

import java.io.PrintWriter;

public class HttpResponse {

    private int statusCode;
    private String statusMessage;
    private String body;

    public HttpResponse() {
        this.statusCode = 200;
        this.statusMessage = "OK";
    }

    public void send(PrintWriter out) {
        out.println("HTTP/1.1 " + statusCode + " " + statusMessage);
        if (body != null) {
            out.println(body);
        }
        out.flush();
    }

}