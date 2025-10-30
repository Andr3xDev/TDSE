package edu.escuelaing.twitter.controller;

import edu.escuelaing.twitter.model.Stream;
import edu.escuelaing.twitter.service.StreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stream")
@CrossOrigin(origins = "*")
public class StreamController {
    @Autowired
    private StreamService streamService;

    @GetMapping
    public Stream getStream() {
        return streamService.getGlobalStream();
    }
}
