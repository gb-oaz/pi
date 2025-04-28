package com.pi.utils.constants;

/**
 * A utility class that defines constant values for HTTP request headers and parameters.
 * <p>
 * These constants are used throughout the application to standardize the names
 * of commonly used request headers and parameters.
 * </p>
 */
public class Request {

    /**
     * Represents the "Authorization" HTTP header, typically used for passing
     * authentication credentials.
     */
    public static final String AUTHORIZATION = "Authorization";

    /**
     * Represents the "commandType" request parameter, used to specify the type
     * of command being executed.
     */
    public static final String COMMAND_TYPE = "commandType";

    /**
     * Represents the "queryType" request parameter, used to specify the type
     * of query being performed.
     */
    public static final String QUERY_TYPE = "queryType";

    /**
     * Represents the "page" request parameter, used to specify the page number
     * for pagination.
     */
    public static final String PAGE = "page";

    /**
     * Represents the "size" request parameter, used to specify the page size
     * for pagination.
     */
    public static final String SIZE = "size";

    /**
     * Represents the "keyLive" request parameter, used to specify the key of the live class.
     */
    public static final String KEY_LIVE = "keyLive";

    /**
     * Represents the "quizKey" request parameter, used to specify the key of the quiz.
     */
    public static final String KEY_QUIZ = "keyQuiz";

    /**
     * Represents the "pupilLogin" request parameter, used to specify the login of the pupil.
     */
    public static final String PUPIL_LOGIN = "pupilLogin";

    /**
     * Represents the "pupilCode" request parameter, used to specify the code of the pupil.
     */
    public static final String PUPIL_CODE = "pupilCode";

    /**
     * Represents the "answerItem" request parameter, used to specify the item of the answer.
     */
    public static final String ANSWER_ITEM = "answerItem";

    /**
     * Represents the "login" request parameter, used to specify the login of the user.
     */
    public static final String LOGIN = "login";

    /**
     * Represents the "code" request parameter, used to specify the code of the user.
     */
    public static final String CODE = "code";
}
