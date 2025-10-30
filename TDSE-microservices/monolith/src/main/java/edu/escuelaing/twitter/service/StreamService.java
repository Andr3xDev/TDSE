package edu.escuelaing.twitter.service;

import edu.escuelaing.twitter.model.Stream;
import edu.escuelaing.twitter.repository.StreamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreamService {
    @Autowired
    private StreamRepository streamRepository;

    private static final String GLOBAL_STREAM_NAME = "Global Feed";

    public Stream getGlobalStream() {
        return streamRepository.findByName(GLOBAL_STREAM_NAME).orElseThrow(() -> new RuntimeException("Stream not found"));
    }
}