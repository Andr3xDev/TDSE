package edu.escuelaing.tdse.utils;

/**
 * Custom exception for user-related operations. This exception is thrown when user validation,
 * authentication, or registration fails.
 */
public class UserException extends Exception {

    public static final String USER_NOT_FOUND = "User not found";
    public static final String PASSWORD_INVALID = "Invalid password";
    public static final String PASSWORD_NULL = "Password cannot be null or empty";
    public static final String EMAIL_ALREADY_REGISTERED = "Email already registered";
    public static final String PASSWORD_NOT_VALID = "Password not valid";

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

}
