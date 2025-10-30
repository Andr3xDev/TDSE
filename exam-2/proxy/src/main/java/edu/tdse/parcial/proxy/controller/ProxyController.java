package edu.tdse.parcial.proxy.controller;

import edu.tdse.parcial.proxy.network.HttpConnection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ProxyController {

    private static int machine = 1;

    /**
     * Endpoint to calculate Collatz Sequence
     *
     * @param value value to start sequence
     * @return sequence full
     */
    @GetMapping("sequence")
    public String secuence(@RequestParam(value = "value") Integer value) throws IOException {
        if (machine == 1){
            machine = 2;
            return getSequence("http://54.145.169.145:8080/sequence?value=13");
        } else {
            machine = 1;
            return getSequence("http://34.202.229.223:8080/sequence?value=13");
        }
    }

    private String getSequence(String url) throws IOException {
        return HttpConnection.getAll(url);
    }
}