package com.pi.core_auth.core.utils.constants;

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
}
