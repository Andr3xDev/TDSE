package edu.escuelaing.tdse;

import edu.escuelaing.tdse.backend.HttpServer;
import edu.escuelaing.tdse.facade.FacadeServer;

public class Start {
    public static void main(String[] args) {
        try {
            while (true) {
                if (args[0] == "server") {
                    System.err.println("Getting backend server");
                    HttpServer.main(args);
                } else {
                    System.err.println("Getting facade server");
                    FacadeServer.main(args);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
