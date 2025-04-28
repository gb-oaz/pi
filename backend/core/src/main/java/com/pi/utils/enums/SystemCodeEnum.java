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

    // RANGE Query dto auth---- //
    C010PI("Field query is necessary", "Provide correct value, string example: | GET_STATUS_TOKEN |"),
    C011PI("Field token is necessary", "Provide correct value, string example: | Bearer <your_token> |"),

    // RANGE Command dto auth---- //
    C030PI("Field command is necessary", "Provide correct value, string example: | POST_SIGN_IN_TOKEN or POST_ANONYMOUS_TOKEN |"),
    C031PI("Field login is necessary", "Provide correct value, the login must be between 8 and 15 characters uppercase letters"),
    C032PI("Field code is necessary", "Provide correct value, the code must be at least 6 numbers"),
    C033PI("Field password is necessary", "Provide correct value, the password must between 15 and 25 characters long, containing at least one lowercase letter, one uppercase letter, one digit, and one special character !?@#$%&"),

    // RANGE Authorization and Authentication ---- //
    C050PI("Error in Authentication", "Your user not have access to this resource"),
    C051PI("Error in Authorization", "Your user not have permission to this resource"),
    C052PI("User email already exist", "Introduce another credentials email to create a new user"),
    C053PI("User login and code already exist", "Introduce another credentials login and code to create a new user"),
    CO54PI("Old password not match", "Check your password"),

    // RANGE Token ---- //
    C060PI("Unknown result token validation", "Check your token format"),

    // RANGE Query dto user---- //
    C070PI("Field query is necessary", "Provide correct value, string example: | GET_USER_BY_PROJECTION or GET_USERS_BY_PROJECTION |"),
    C071PI("At least one field is required", "Provide field email or login and code is required"),

    // RANGE Command dto user---- //
    C080PI("Field command is necessary", "Provide correct value, string example: | POST_CREATE_USER_TEACHER, POST_CREATE_USER_STUDENT or PUT_UPDATE_USER |"),
    C081PI("Field name is necessary", "Provide correct value, the name must be between 3 and 40 characters"),
    C082PI("Field email is necessary", "Provide correct value, the email example: | <your_email>@<domain>.<tld> |"),

    // RAGE quiz ---- //
    C090PI("Field type quiz is necessary", "Provide correct value, string example: | QUIZ_MULTIPLE_CHOICE, QUIZ_FILL_SPACE, QUIZ_TRUE_FALSE, QUIZ_OPEN, QUIZ_POLL, QUIZ_WORD_CLOUD |"),
    C091PI("Field in quizItem is necessary", "Provide field quizItem is required"),
    C092PI("Field type slide is necessary", "Provide correct value, string example: | SLIDE_TITLE_1, SLIDE_TITLE_2, SLIDE_TEXT_1, SLIDE_TEXT_2, SLIDE_TEXT_MEDIA_1, SLIDE_TEXT_MEDIA_2 |"),

    // RANGE Command dto quiz ---- //
    C100PI("Field command is necessary", "Provide correct value, string example: | COMMAND_POST_NEW_QUIZ, COMMAND_POST_QUIZ_ITEM, COMMAND_PATCH_QUIZ_ITEM, COMMAND_PUT_QUIZ, COMMAND_DELETE_QUIZ_ITEM, COMMAND_DELETE_QUIZ |"),
    C101PI("Field key is necessary", "Provide key value, string example: | <key_quiz> |"),
    C102PI("Field position is necessary", "Provide position value, integer example: | 1 or 2 |"),
    C103PI("Field name is necessary", "Provide position value, string example: | <name_quiz> |"),
    C104PI("Field categories is necessary", "Provide categories value, string example: | <category_1>, <category_2> |"),

    // RAGE Query dto quiz ---- //
    C110PI("Field queryType is necessary", "Provide correct value, string example: | QUERY_GET_QUIZ, QUERY_GET_QUIZ_PROJECTION, QUERY_GET_QUIZ_ITEM |"),

    // RAGE Query dto live ---- //
    C120PI("Field queryType is necessary", "Provide correct value, string example: | QUERY_GET_LIVE |"),
    C121PI("Field key is necessary", "Provide key live value, string example: | <key_quiz> |"),

    // RAGE Command dto live ---- //
    C130PI("Field commandType is necessary", "Provide correct value, string example: | COMMAND_POST_NEW_LIVE, COMMAND_PATCH_NEXT_POSITION, COMMAND_PATCH_PREVIOUS_POSITION, COMMAND_PATCH_REMOVE_PUPIL_FROM_LOBBY, COMMAND_PATCH_ADD_PUPIL_TO_LOBBY, COMMAND_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ, COMMAND_PATCH_END_LIVE |"),
    ;

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