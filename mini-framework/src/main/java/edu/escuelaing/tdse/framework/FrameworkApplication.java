package edu.escuelaing.tdse.framework;

import static edu.escuelaing.tdse.framework.HttpServer.get;
import static edu.escuelaing.tdse.framework.HttpServer.post;
import static edu.escuelaing.tdse.framework.HttpServer.staticFiles;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * The main entry point for the TDSE Framework application.
 * <p>
 * This class configures HTTP routes and static file serving for the framework.
 * </p>
 * <ul>
 * <li>Serves static files from the <code>resources/static</code>
 * directory.</li>
 * <li>Defines GET endpoints:
 * <ul>
 * <li><code>/app/hellow</code>: Returns "Hello, world".</li>
 * <li><code>/app</code>: Returns a message with the <code>name</code> query
 * parameter.</li>
 * <li><code>/app/pi</code>: Returns the value of PI.</li>
 * </ul>
 * </li>
 * <li>Defines a POST endpoint:
 * <ul>
 * <li><code>/app/hello</code>: Returns a message with the <code>name</code>
 * query parameter.</li>
 * </ul>
 * </li>
 * </ul>
 *
 * @author Andrés Chavarro
 */
public class FrameworkApplication {

    public static void main(String[] args) throws IOException, URISyntaxException {
        staticFiles("resources/static");
        get("/app/hellow", (req, resp) -> "Hello, world");
        get("/app", (req, resp) -> "Get received: " + req.getQueryParam("name"));
        get("/app/pi", (req, resp) -> String.valueOf(Math.PI));
        post("/app/hello", (req, resp) -> "Post received: " + req.getQueryParam("name"));
        HttpServer.main(args);
    }

}