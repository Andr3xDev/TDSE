package edu.escuelaing.tdse.service;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import edu.escuelaing.tdse.model.User;
import edu.escuelaing.tdse.repository.UserRepository;
import edu.escuelaing.tdse.utils.UserException;
import lombok.RequiredArgsConstructor;

/**
 * Implements the contract from the interface, defines the business logic and validations for user
 * management operations. Handles user registration, authentication, and password encryption using
 * BCrypt.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * {@inheritDoc}
     */
    @Override
    public User createUser(User userDTO) throws UserException {
        String emailLowerCase = userDTO.getEmail().toLowerCase().trim();

        if (userRepository.findByEmail(emailLowerCase).isPresent()) {
            throw new UserException(UserException.EMAIL_ALREADY_REGISTERED);
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new UserException(UserException.PASSWORD_NOT_VALID);
        }
        String hashPassword = encoder.encode(userDTO.getPassword());
        User newUser = new User(userDTO.getName(), hashPassword, emailLowerCase);
        userRepository.save(newUser);
        return new User(newUser.getId(), newUser.getName(), hashPassword, newUser.getEmail());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean login(User userDTO) throws UserException {
        User userStored = getUser(userDTO);

        if (verify(userDTO.getPassword(), userStored.getPassword())) {
            return true;
        }
        throw new UserException(UserException.PASSWORD_INVALID);
    }

    /**
     * Retrieves a user from the database by their email address. Converts the email to lowercase
     * for case-insensitive lookup.
     *
     * @param userDTO The user entity containing the email to search for.
     * @return The {@link User} entity found in the database.
     * @throws UserException if no user is found with the provided email.
     */
    public User getUser(User userDTO) throws UserException {
        String emailLowerCase = userDTO.getEmail().toLowerCase().trim();
        Optional<User> user = userRepository.findByEmail(emailLowerCase);
        if (user.isEmpty()) {
            throw new UserException(UserException.USER_NOT_FOUND);
        }
        return user.get();
    }

    /**
     * Verifies that a raw password matches a stored hashed password. Uses BCrypt's secure password
     * matching algorithm.
     *
     * @param rawPassword The plain text password provided by the user.
     * @param storedPassword The BCrypt hashed password stored in the database.
     * @return {@code true} if the passwords match.
     * @throws UserException if the raw password is null, empty, or does not match the stored
     *         password.
     */
    public boolean verify(String rawPassword, String storedPassword) throws UserException {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new UserException(UserException.PASSWORD_NULL);
        }
        if (!encoder.matches(rawPassword, storedPassword)) {
            throw new UserException(UserException.PASSWORD_INVALID);
        }
        return true;
    }

}
