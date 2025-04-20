package com.pi.core_user.core.utils.constants;

/**
 * The {@code Router} class defines constant values for API routes used in the user microservice.
 * These constants ensure consistent use of route paths across the application.
 *
 * @author GustavoBoaz
 * @since 1.0
 */
public class Router {
    /**
     * The route for fetching the info of the user microservice.
     * This route points to the "/user/info" endpoint, which provides details about the microservice status and version.
     */
    public static final String ROUTER_USER_INFO = "/user/info";

    /**
     * The route for fetching a user by projection.
     */
    public static final String ROUTER_GET_USER_BY_PROJECTION = "/user/v1/get/user/by/projection";

    /**
     * The route for fetching a list of users by projection.
     */
    public static final String ROUTER_GET_USERS_BY_PROJECTION = "/user/v1/get/users/by/projection";

    /**
     * The route for creating a new user teacher.
     */
    public static final String ROUTER_POST_CREATE_USER_TEACHER = "/user/v1/post/create/user/teacher";

    /**
     * The route for creating a new user student.
     */
    public static final String ROUTER_POST_CREATE_USER_STUDENT = "/user/v1/post/create/user/student";

    /**
     * The route for updating an existing user.
     */
    public static final String ROUTER_PUT_UPDATE_USER = "/user/v1/put/update/user";
}
