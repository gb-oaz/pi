package com.pi.core_auth.core.utils.constants;

/**
 * The {@code Router} class defines constant values for API routes used in the auth microservice.
 * These constants ensure consistent use of route paths across the application.
 *
 * @author GustavoBoaz
 * @since 1.0
 */
public class Router {
    /**
     * The route for fetching the info of the auth microservice.
     * This route points to the "/auth/info" endpoint, which provides details about the microservice status and version.
     */
    public static final String SERVER_INFO = "/auth/info";

    /**
     * The route for getStatusToken.
     * This route points to the "/auth/v1/get/status/token" endpoint.
     */
    public static final String GET_STATUS_TOKEN = "/auth/v1/get/status/token";

    /**
     * The route for getScopeToken.
     * This route points to the "/auth/v1/get/scope/token" endpoint.
     */
    public static final String GET_SCOPE_TOKEN = "/auth/v1/get/scope/token";

    /**
     * The route for postSignInToken.
     * This route points to the "/auth/v1/post/sign/in/token" endpoint.
     */
    public static final String POST_SIGN_IN_TOKEN = "/auth/v1/post/sign/in/token";

    /**
     * The route for postAnonymousToken.
     * This route points to the "/auth/v1/post/anonymous/token" endpoint.
     */
    public static final String POST_ANONYMOUS_TOKEN = "/auth/v1/post/anonymous/token";
}
