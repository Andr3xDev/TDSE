package edu.tdse.parcial.math.controllers;

import edu.tdse.parcial.math.serivice.MathService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class MathController {

    private final MathService mathService;

    public MathController(MathService mathService){
        this.mathService = mathService;
    }

    /**
     * Endpoint to calculate Collatz Sequence
     *
     * @param value value to start sequence
     * @return sequence full
     */
    @GetMapping("sequence")
    public List<Integer> secuence(@RequestParam(value = "value") Integer value) {
        return mathService.collatzSequence(value);
    }

}