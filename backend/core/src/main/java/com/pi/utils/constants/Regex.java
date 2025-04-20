package com.pi.utils.constants;

/**
 * The {@code Regex} class contains regular expression patterns for validating login and password.
 * <p>
 * These patterns can be used to ensure that logins and passwords meet the specified security requirements.
 * </p>
 *
 * @author GustavoBoaz
 * @since 1.0
 */
public class Regex {

    /**
     * Regular expression pattern for validating login.
     * The login must be between 8 and 15 characters in capital letters.
     */
    public static final String LOGIN = "^[A-Z]{8,15}$";

    /**
     * Regular expression pattern for validating code.
     * The code must be at least 6 numbers.
     */
    public static final String CODE = "^[0-9]{6,6}$";

    /**
     * Regular expression pattern for validating password.
     * The password must be between 15 and 25 characters long, containing at least one lowercase letter,
     * one uppercase letter, one digit, and one special character !?@#$%&.
     */
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!?@#$%&]).{15,25}$";

    /**
     * Regular expression pattern for validating name.
     * The name must be between 3 and 40 characters with space.
     */
    public static final String NAME = "^[a-zA-ZÀ-ÿ\\s]{3,40}$";

    /**
     * Regular expression pattern for validating email.
     * The email must be in a valid format.
     */
    public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
}