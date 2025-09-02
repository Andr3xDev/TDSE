package edu.escuelaing.tdse.framework.controllers;

import edu.escuelaing.tdse.framework.annotations.GetMapping;
import edu.escuelaing.tdse.framework.annotations.RequestParam;
import edu.escuelaing.tdse.framework.annotations.RestController;

/**
 * Controller that handles greeting-related HTTP requests.
 * 
 * Provides endpoints to return personalized and static greeting messages.
 * 
 * Endpoints:
 * <ul>
 * <li><b>/greeting</b>: Returns a personalized greeting message. Accepts an
 * optional <code>name</code> query parameter.</li>
 * <li><b>/hello</b>: Returns a static greeting message.</li>
 * </ul>
 */
@RestController
public class GrettingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "world") String name) {
        return "Hello " + name + " !";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }

}