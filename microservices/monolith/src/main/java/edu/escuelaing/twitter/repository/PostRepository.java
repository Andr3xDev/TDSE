package edu.escuelaing.twitter.repository;

import edu.escuelaing.twitter.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}