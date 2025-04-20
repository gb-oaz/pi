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
}
