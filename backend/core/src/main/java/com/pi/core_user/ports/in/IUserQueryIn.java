package com.pi.core_user.ports.in;

import com.pi.core_user.core.domains.User;
import com.pi.core_user.core.utils.constants.Router;
import com.pi.utils.constants.Query;
import com.pi.utils.constants.Request;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.Pageable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Interface for handling user-related queries.
 * <p>
 *     This interface defines the contract for executing user-related operations,
 *     such as searching for user.
 * </p>
 */
@Tag(name = "User Query: port In")
public interface IUserQueryIn {

    /**
     * Retrieve the user by projection.
     * <p>
     * This endpoint is used to retrieve the user by projection. The query type
     * must be {@code QUERY_GET_USER_BY_PROJECTION}.
     * </p>
     * <p>
     * Example Path JSON:
     * <pre>
     * {
     *     "path": "/user/v1/get/user/by/projection/QUERY_GET_USER_BY_PROJECTION"
     * }
     * </pre>
     * Example Headers JSON:
     * <pre>
     * {
     *     "Authorization": "Bearer &lt;token&gt;"
     * }
     * </pre>
     * Example Query Parameters JSON:
     * <pre>
     * {
     *     "email": "gustavo.boaz@domainpi.com",
     *     "login": "GUSTAVOBOAZ",
     *     "code": "123456",
     * }
     * </pre>
     * Example Response Body JSON:
     * <pre>
     * {
     *     "name": "Gustavo Boaz",
     *     "email": "gustavo.boaz@domainpi.com",
     *     "login": "GUSTAVOBOAZ",
     *     "code": "123456",
     *     "createAt": "2023-08-01T00:00:00.000Z",
     *     "updateAt": "2023-08-01T00:00:00.000Z"
     * }
     * </pre>
     * </p>
     *
     * @param queryType the type of query being performed, must be {@code GET_SCOPE_TOKEN}
     * @param authorization the authorization header containing the bearer token
     * @param email the email of the user
     * @param login the login of the user
     * @param code the code of the user
     * @return a {@link ResponseEntity} containing the scope types if valid
     * @throws GlobalException if the token is invalid or an error occurs
     */
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Retrieve the user by projection
            Use this endpoint to retrieve user by projection.
            - The queryType must be QUERY_GET_USER_BY_PROJECTION.
            - The token must be a valid token.
            - The email, login and code must be provided in the request params.
            
            **Example Path JSON:**
            ```json
            {
                path: "/user/v1/get/user/by/projection/QUERY_GET_USER_BY_PROJECTION"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <token>",
            }
            ```
            
            **Example Request Params JSON:**
            ```json
            {
                "email": "gustavo.boaz@domainpi.com",
                "login": "GUSTAVOBOAZ",
                "code": "123456",
            }
            ```
            
            **Example Response Body JSON:**
            ```json
            {
                "name": "Gustavo Boaz",
                "email": "gustavo.boaz@domainpi.com",
                "login": "GUSTAVOBOAZ",
                "code": "123456",
                "createAt": "2024-09-23T10:00:00Z",
                "updateAt": "2024-09-23T10:00:00Z"
            }

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token scope retrieved successfully", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class))
                    })
            }
    )
    @GetMapping(path = Router.ROUTER_GET_USER_BY_PROJECTION + "/{queryType}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('SCOPE_ANONYMOUS', 'SCOPE_TEACHER', 'SCOPE_STUDENT')")
    ResponseEntity<User> getUserByProjection(
            @PathVariable(name = Request.QUERY_TYPE) String queryType,
            @RequestHeader(name = Request.AUTHORIZATION) String authorization,
            @RequestParam(name = Query.EMAIL, required = false) String email,
            @RequestParam(name = Query.LOGIN, required = false) String login,
            @RequestParam(name = Query.CODE, required = false) String code
    ) throws GlobalException;

    /**
     * Retrieve the users by projection.
     * <p>
     * This endpoint is used to retrieve the users by projection. The query type
     * must be {@code QUERY_GET_USERS_BY_PROJECTION}.
     * </p>
     * <p>
     * Example Path JSON:
     * <pre>
     * {
     *     "path": "/user/v1/get/users/by/projection/QUERY_GET_USERS_BY_PROJECTION"
     * }
     * </pre>
     * Example Headers JSON:
     * <pre>
     * {
     *     "Authorization": "Bearer &lt;token&gt;"
     * }
     * </pre>
     * Example Query Parameters JSON:
     * <pre>
     * {
     *     "name": "Gustavo Boaz",
     *     "email": "gustavo.boaz@domainpi.com",
     *     "login": "GUSTAVOBOAZ",
     *     "code": "123456",
     *     "page": 0,
     *     "size": 10
     * }
     * </pre>
     * Example Response Body JSON:
     * <pre>
     *     {
     *          "content": [
     *              {
     *                  "name": "Gustavo Boaz",
     *                  "email": "gustavo.boaz@domainpi.com",
     *                  "login": "GUSTAVOBOAZ",
     *                  "code": "123456",
     *                  "createAt": "2023-08-01T00:00:00.000Z",
     *                  "updateAt": "2023-08-01T00:00:00.000Z"
     *              }
     *          ],
     *          "pageable": {
     *              "page": 0,
     *              "size": 10,
     *              "total": 1
     *          },
     *     }
     * </pre>
     * </p>
     *
     * @param queryType the type of query being performed, must be {@code GET_SCOPE_TOKEN}
     * @param authorization the authorization header containing the bearer token
     * @param name the name of the user
     * @param email the email of the user
     * @param login the login of the user
     * @param code the code of the user
     * @param page the page number
     * @param size the size of the page
     * @return a {@link ResponseEntity} containing the scope types if valid
     * @throws GlobalException if the token is invalid or an error occurs
     */
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Retrieve the users by projection
            Use this endpoint to retrieve users by projection.
            - The queryType must be QUERY_GET_USERS_BY_PROJECTION.
            - The token must be a valid token.
            - The name, email, login and code must be provided in the request params.
            
            **Example Path JSON:**
            ```json
            {
                path: "/user/v1/get/users/by/projection/QUERY_GET_USERS_BY_PROJECTION"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <token>",
            }
            ```
            
            **Example Request Params JSON:**
            ```json
            {
                "name": "Gustavo Boaz",
                "email": "gustavo.boaz@domainpi.com",
                "login": "GUSTAVOBOAZ",
                "code": "123456",
                "page": 0,
                "size": 10
            }
            ```
            
            **Example Response Body JSON:**
            ```json
            {
                "content": [
                    {
                        "name": "Gustavo Boaz",
                        "email": "gustavo.boaz@domainpi.com",
                        "login": "GUSTAVOBOAZ",
                        "code": "123456",
                        "createAt": "2024-09-23T10:00:00Z",
                        "updateAt": "2024-09-23T10:00:00Z"
                    }
                ],
                "pageable": {
                    "page": 0,
                    "size": 10,
                    "total": 1
                }
            }
            ```
            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token scope retrieved successfully", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Pageable.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class))
                    }),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class))
                    })
            }
    )
    @GetMapping(path = Router.ROUTER_GET_USERS_BY_PROJECTION + "/{queryType}", produces = "application/json")
    @PreAuthorize("hasAnyAuthority('SCOPE_ANONYMOUS', 'SCOPE_TEACHER', 'SCOPE_STUDENT')")
    ResponseEntity<Pageable<User>> getUsersByProjection(
            @PathVariable(name = Request.QUERY_TYPE) String queryType,
            @RequestHeader(name = Request.AUTHORIZATION) String authorization,
            @RequestParam(name = Query.NAME, required = false) String name,
            @RequestParam(name = Query.EMAIL, required = false) String email,
            @RequestParam(name = Query.LOGIN, required = false) String login,
            @RequestParam(name = Query.CODE, required = false) String code,
            @RequestParam(name = Request.PAGE, required = false, defaultValue = "0") Integer page,
            @RequestParam(name = Request.SIZE, required = false, defaultValue = "10") Integer size
    ) throws GlobalException;
}
