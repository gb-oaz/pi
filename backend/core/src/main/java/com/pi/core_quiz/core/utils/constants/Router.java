package com.pi.core_quiz.core.utils.constants;

/**
 * The {@code Router} class defines constant values for API routes used in the quiz microservice.
 * These constants ensure consistent use of route paths across the application.
 *
 * @author GustavoBoaz
 * @since 1.0
 */
public class Router {
    /**
     * The route for fetching the info of the quiz microservice.
     * This route points to the "/quiz/info" endpoint, which provides details about the microservice status and version.
     */
    public static final String ROUTER_QUIZ_INFO = "/quiz/info";

    /**
     * The route for fetching a quiz by its key.
     * This route points to the "/quiz/v1/get/quiz/{key}" endpoint, where {key} is a placeholder for the quiz key.
     */
    public static final String ROUTER_GET_QUIZ = "/quiz/v1/get/quiz/{key}";

    /**
     * The route for fetching a quiz projection.
     * This route points to the "/quiz/v1/get/quizes/projection" endpoint, which retrieves a projection of quizzes.
     */
    public static final String ROUTER_GET_QUIZES_PROJECTION = "/quiz/v1/get/quizes/projection";

    /**
     * The route for fetching a quiz item by its key and position.
     * This route points to the "/quiz/v1/get/quiz/{key}/item/{position}" endpoint, where {key} is the quiz key and {position} is the item's position.
     */
    public static final String ROUTER_GET_QUIZ_ITEM = "/quiz/v1/get/quiz/{key}/item/{position}";

    /**
     * The route for posting a new quiz.
     * This route points to the "/quiz/v1/post/new/quiz" endpoint, which allows the creation of a new quiz.
     */
    public static final String ROUTER_POST_NEW_QUIZ = "/quiz/v1/post/new/quiz";

    /**
     * The route for posting a quiz item.
     * This route points to the "/quiz/v1/post/quiz/{key}/item/{position}" endpoint, where {key} is the quiz key and {position} is the item's position.
     */
    public static final String ROUTER_POST_QUIZ_ITEM = "/quiz/v1/post/quiz/{key}/item/{position}";

    /**
     * The route for patching a quiz item.
     * This route points to the "/quiz/v1/patch/quiz/{key}/item/{position}" endpoint, where {key} is the quiz key and {position} is the item's position.
     */
    public static final String ROUTER_PATCH_QUIZ_ITEM = "/quiz/v1/patch/quiz/{key}/item/{position}";

    /**
     * The route for putting a quiz.
     * This route points to the "/quiz/v1/put/quiz/{key}" endpoint, where {key} is the quiz key.
     */
    public static final String ROUTER_PUT_QUIZ = "/quiz/v1/put/quiz/{key}";

    /**
     * The route for deleting a quiz item.
     * This route points to the "/quiz/v1/delete/quiz/{key}/item/{position}" endpoint, where {key} is the quiz key and {position} is the item's position.
     */
    public static final String ROUTER_DELETE_QUIZ_ITEM = "/quiz/v1/delete/quiz/{key}/item/{position}";

    /**
     * The route for deleting a quiz.
     * This route points to the "/quiz/v1/delete/quiz/{key}" endpoint, where {key} is the quiz key.
     */
    public static final String ROUTER_DELETE_QUIZ = "/quiz/v1/delete/quiz/{key}";
}
