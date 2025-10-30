package edu.escuelaing.twitter.repository;

import edu.escuelaing.twitter.model.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StreamRepository extends JpaRepository<Stream, Long> {
    Optional<Stream> findByName(String name);
}