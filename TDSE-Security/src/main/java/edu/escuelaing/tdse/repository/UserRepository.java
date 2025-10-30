package edu.escuelaing.tdse.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.escuelaing.tdse.model.User;

/**
 * Repository interface for managing {@link User} entities.
 * <p>
 * Extends {@link JpaRepository} to provide standard CRUD operations and query methods for the User
 * entity.
 * </p>
 * <p>
 * This interface is a Spring Data JPA repository, and Spring will automatically provide the
 * implementation at runtime.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address. Email lookup is case-sensitive at the database level,
     * but the service layer normalizes emails to lowercase before queries.
     *
     * @param email The email address to search for.
     * @return An {@link Optional} containing the found user, or empty if no user exists with that
     *         email.
     */
    Optional<User> findByEmail(String email);

}
