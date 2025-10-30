package edu.escuelaing.stream;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@Data
public class Stream {
    private Long id = 1L;  // Fijo para global
    private String name = "Global Feed";
    private List<Map<String, Object>> posts = new ArrayList<>();
}