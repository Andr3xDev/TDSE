package edu.escuelaing.tdse.service;

import edu.escuelaing.tdse.model.User;
import edu.escuelaing.tdse.utils.UserException;

/**
 * Defines the contract for business logic operations related to user management. This interface
 * abstracts user registration and authentication operations, allowing for a decoupled
 * implementation.
 */
public interface UserService {

    /**
     * Creates a new user in the system from the provided data. The user's password will be securely
     * hashed before storage.
     *
     * @param user The entity containing the user information (name, email, password). Must not be
     *        null.
     * @return A {@link User} entity representing the newly created user, including its generated ID
     *         and hashed password.
     * @throws UserException if the email is already registered or if the password is invalid.
     */
    User createUser(User user) throws UserException;

    /**
     * Authenticates a user by validating their credentials. Checks if the provided email exists and
     * if the password matches the stored hashed password.
     *
     * @param user The entity containing the login credentials (email and password). Must not be
     *        null.
     * @return {@code true} if authentication is successful.
     * @throws UserException if the user is not found or if the password is invalid.
     */
    boolean login(User user) throws UserException;

}
