package edu.escuelaing.tdse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import edu.escuelaing.tdse.model.User;
import edu.escuelaing.tdse.service.UserServiceImpl;
import edu.escuelaing.tdse.utils.UserException;
import lombok.RequiredArgsConstructor;

/**
 * REST Controller for handling all HTTP requests related to the User resource. All endpoints are
 * mapped under the base path "/api/users". Handles user registration and authentication operations.
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    /**
     * Handles POST requests to register a new user in the system. The password will be securely
     * hashed before storage.
     *
     * @param user The request body containing the user data (name, email, password).
     * @return A {@link ResponseEntity} with the created {@link User} and a 200 OK status on
     *         success, or a 400 Bad Request with an error message if validation fails.
     */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.ok(createdUser);
        } catch (UserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Handles POST requests to authenticate a user. Validates the user's credentials (email and
     * password).
     *
     * @param userDTO The request body containing the login credentials (email and password).
     * @return A {@link ResponseEntity} with {@code true} and a 200 OK status on successful
     *         authentication, or a 401 Unauthorized with an error message if authentication fails.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User userDTO) {
        try {
            boolean authenticatedUser = userService.login(userDTO);
            return ResponseEntity.ok(authenticatedUser);
        } catch (UserException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

}
