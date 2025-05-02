package com.pi.core_live.core.utils.constants;

/**
 * The {@code Router} class defines constant values for API routes used in the live microservice.
 * These constants ensure consistent use of route paths across the application.
 *
 * @author GustavoBoaz
 * @since 1.0
 */
public class Router {
    /**
     * The route for fetching the info of the live microservice.
     * This route points to the "/live/info" endpoint, which provides details about the microservice status and version.
     */
    public static final String ROUTER_LIVE_INFO = "/live/info";

    /**
     * The route for fetching a specific live.
     */
    public static final String ROUTER_GET_LIVE = "/live/v1/get/live";

    /**
     * The route for fetching a specific live stream.
     */
    public static final String ROUTER_GET_LIVE_STREAM = "/live/v1/get/live/stream";

    /**
     * The route for creating a new live.
     */
    public static final String ROUTER_POST_NEW_LIVE = "/live/v1/post/new/live";

    /**
     * The route for updating the next position of a live.
     */
    public static final String ROUTER_PATCH_NEXT_POSITION = "/live/v1/patch/next/position";

    /**
     * The route for updating the previous position of a live.
     */
    public static final String ROUTER_PATCH_PREVIOUS_POSITION = "/live/v1/patch/previous/position";

    /**
     * The route for updating the add a pupil to the lobby.
     */
    public static final String ROUTER_PATCH_ADD_PUPIL_TO_LOBBY = "/live/v1/patch/add/pupil/to/lobby";

    /**
     * The route for updating the remove a pupil from the lobby.
     */
    public static final String ROUTER_PATCH_REMOVE_PUPIL_FROM_LOBBY = "/live/v1/patch/remove/pupil/from/lobby";

    /**
     * The route for updating the answer a pupil to the quiz.
     */
    public static final String ROUTER_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ = "/live/v1/patch/add/pupil/answer/to/quiz";

    /**
     * The route for updating the end a live.
     */
    public static final String ROUTER_PATCH_END_LIVE = "/live/v1/patch/end/live";
}
