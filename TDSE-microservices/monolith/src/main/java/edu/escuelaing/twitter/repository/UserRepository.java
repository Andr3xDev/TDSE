package edu.escuelaing.twitter.repository;

import edu.escuelaing.twitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByCognitoId(String cognitoId);
}
