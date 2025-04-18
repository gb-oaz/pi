package com.pi.utils.enums;

/**
 * Enum for feature Global Custom alert
 *
 * <p>
 * The {@code SystemCodeEnum} is designed to define standard system codes with associated
 * problem descriptions and recommended actions. This enum is particularly useful for
 * handling and categorizing error messages across the system.
 * Example usage:
 *
 * <pre>
 * {@code
 *   SystemCodeEnum code = SystemCodeEnum.C001PI;
 *   String problem = code.getProblem(); // "Internal server error"
 *   String action = code.getAction();   // "Try a connection at another time"
 * }
 * </pre>
 *
 * You can also use this enum in exception handling or logging to standardize
 * error messages and suggested user actions.
 * Whenever a new error or potential business-related error needs to be mapped
 * in the system, it is important to add a new code to this enum. This ensures
 * consistency in how errors are handled and reported across the application.
 * </p>
 *
 * @author GustavoBoaz
 * @version 1.0.0
 **/
public enum SystemCodeEnum {
    // RANGE resources in application ---- //
    C001PI("Internal server error", "Try a connection at another time"),
    C002PI("Failed generate key pair", "Contact team developer"),
    C003PI("Not found", "The resource is not available"),

    // RANGE Query dto ---- //
    C010PI("Field query is necessary", "Provide correct value, string example: | GET_STATUS_TOKEN |"),
    C011PI("Field token is necessary", "Provide correct value, string example: | Bearer <your_token> |"),

    // RANGE Command dto ---- //
    C030PI("Field command is necessary", "Provide correct value, string example: | POST_SIGN_IN_TOKEN or POST_ANONYMOUS_TOKEN |"),
    C031PI("Field login is necessary", "Provide correct value, the login must be between 8 and 15 characters uppercase letters"),
    C032PI("Field code is necessary", "Provide correct value, the code must be at least 6 numbers"),
    C033PI("Field password is necessary", "Provide correct value, the password must between 15 and 25 characters long, containing at least one lowercase letter, one uppercase letter, one digit, and one special character !?@#$%&"),

    // RANGE Authorization and Authentication ---- //
    C050PI("Error in Authentication", "Your user not have access to this resource"),
    C051PI("Error in Authorization", "Your user not have permission to this resource"),

    // RANGE Token ---- //
    C060PI("Unknown result token validation", "Check your token format"),;

    private final String problem;
    private final String action;

    SystemCodeEnum(String problem, String action) {
        this.problem = problem;
        this.action = action;
    }

    public String getProblem() {
        return this.problem;
    }

    public String getAction() {
        return this.action;
    }
}