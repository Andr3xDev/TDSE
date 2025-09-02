package edu.escuelaing.tdse.framework.controllers;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import edu.escuelaing.tdse.framework.annotations.GetMapping;
import edu.escuelaing.tdse.framework.annotations.PostMapping;
import edu.escuelaing.tdse.framework.annotations.RequestParam;
import edu.escuelaing.tdse.framework.annotations.RestController;

/**
 * Controller for managing a palette of colors.
 * <p>
 * Provides REST endpoints to retrieve and add colors to a shared palette.
 * </p>
 *
 * <ul>
 * <li><b>GET /colors</b>: Returns the current list of colors as a JSON
 * array.</li>
 * <li><b>POST /colors</b>: Adds a new color to the palette. Accepts a "color"
 * request parameter (defaults to "red" if not provided) and returns the updated
 * list.</li>
 * </ul>
 *
 * <p>
 * The palette is stored as a thread-safe list to support concurrent access.
 * </p>
 */
@RestController
public class PaletteController {

    private static final List<String> palette = new CopyOnWriteArrayList<>();

    @GetMapping("/colors")
    public String getColors() {
        String jsonArray = palette.stream()
                .map(color -> "\"" + color + "\"")
                .collect(Collectors.joining(", ", "[", "]"));
        return jsonArray;
    }

    @PostMapping("/colors")
    public String addColor(@RequestParam(value = "color", defaultValue = "red") String newColor) {
        if (newColor != null && !newColor.trim().isEmpty()) {
            palette.add(newColor);
        }
        return getColors();
    }

}