package edu.escuelaing.twitter.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private LocalDateTime timestamp = LocalDateTime.now();

    @ManyToOne
    private User user;
}