package com.pi.core_auth.core.utils.constants;

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
     * The login must be at least 8 characters uppercase letters.
     */
    public static final String LOGIN = "^[A-Z]{8,}$";

    /**
     * Regular expression pattern for validating code.
     * The code must be at least 6 numbers.
     */
    public static final String CODE = "^[0-9]{6,}$";

    /**
     * Regular expression pattern for validating password.
     * The password must be at least 15 characters long, containing at least one lowercase letter,
     * one uppercase letter, one digit, and one special character !?@#$%&.
     */
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!?@#$%&]).{15,}$";
}