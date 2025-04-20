package com.pi.core_auth.ports.in;

import com.pi.core_auth.core.domains.Token;
import com.pi.utils.constants.Command;
import com.pi.utils.constants.Request;
import com.pi.core_auth.core.utils.constants.Router;
import com.pi.utils.exceptions.GlobalException;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;

/**
 * Interface for handling authentication-related commands.
 * <p>
 * This interface defines the contract for executing authentication-related
 * operations, such as generating tokens for sign-in or anonymous access.
 * </p>
 */
@Tag(name = "Auth Commands: port In")
public interface IAuthCommandIn {

    /**
     * Generates a token for a user based on login credentials.
     * <p>
     * This endpoint is used to generate a token for a user after validating
     * their login, code, and password. The command type must be {@code COMMAND_POST_SIGN_IN_TOKEN}.
     * </p>
     * <p>
     * Example Path JSON:
     * <pre>
     * {
     *     "path": "/auth/v1/post/sign/in/token/COMMAND_POST_SIGN_IN_TOKEN"
     * }
     * </pre>
     * Example Headers JSON:
     * <pre>
     * {
     *     "Authorization": "Bearer &lt;your-anonymous-token&gt;"
     * }
     * </pre>
     * Example Request Body Parts JSON:
     * <pre>
     * {
     *     "login": "GUSTAVOBOAZ",
     *     "code": "123456",
     *     "password": "Password@123456"
     * }
     * </pre>
     * Example Response Body JSON:
     * <pre>
     * {
     *     "token": "Bearer &lt;your-access-token&gt;",
     *     "createAt": "2024-09-23T10:00:00Z",
     *     "expiryAt": "2024-09-24T10:00:00Z",
     *     "status": "ACTIVE"
     * }
     * </pre>
     * </p>
     *
     * @param commandType the type of command being executed, must be {@code COMMAND_POST_SIGN_IN_TOKEN}
     * @param authorization the authorization header containing the bearer token
     * @param login the user's login, must be at least 8 characters with uppercase letters
     * @param code the user's code, must be at least 6 numbers
     * @param password the user's password, must be at least 15 characters long with specific requirements
     * @return a {@link ResponseEntity} containing the generated token details
     * @throws GlobalException if validation fails or an error occurs
     */
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Generate Token by Sign In
            Use this endpoint to generate a token for an user based on login credentials.
            - The commandType must be COMMAND_POST_SIGN_IN_TOKEN.
            - The login must be at least 8 characters uppercase letters.
            - The code must be at least 6 numbers.
            - The password must be at least 15 characters long, containing at least one lowercase letter,
                one uppercase letter, one digit, and one special character !?@#$%&.
            - The anonymousToken must be a valid token.
            
            **Example Path JSON:**
            ```json
            {
                path: "/auth/v1/post/sign/in/token/COMMAND_POST_SIGN_IN_TOKEN"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <your-anonymous-token>",
            }
            ```

            **Example Request Body Parts JSON:**
            ```json
            {
                "login": "GUSTAVOBOAZ",
                "code": "123456",
                "password": "Password@123456",
            }
            ```
            
            **Example Response Body JSON:**
            ```json
            {
                "token": "Bearer <your-access-token>",
                "createAt": "2024-09-23T10:00:00Z",
                "expiryAt": "2024-09-24T10:00:00Z",
                "status": "ACTIVE"
            }

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "201", description = "Token generated successfully", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Token.class))
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
    @PostMapping(path = Router.ROUTER_POST_SIGN_IN_TOKEN + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_ANONYMOUS')")
    ResponseEntity<Token> postSignInToken(
            @PathVariable(name = Request.COMMAND_TYPE) String commandType,
            @RequestHeader(name = Request.AUTHORIZATION) String authorization,
            @RequestPart(name = Command.LOGIN) String login,
            @RequestPart(name = Command.CODE) String code,
            @RequestPart(name = Command.PASSWORD) String password
    ) throws GlobalException;

    /**
     * Generates a token for an anonymous user.
     * <p>
     * This endpoint is used to generate a token for an anonymous user. The command type
     * must be {@code COMMAND_POST_ANONYMOUS_TOKEN}.
     * </p>
     * <p>
     * Example Path JSON:
     * <pre>
     * {
     *     "path": "/auth/v1/post/anonymous/token/COMMAND_POST_ANONYMOUS_TOKEN"
     * }
     * </pre>
     * Example Response Body JSON:
     * <pre>
     * {
     *     "token": "Bearer &lt;your-anonymous-access-token&gt;",
     *     "createAt": "2024-09-23T10:00:00Z",
     *     "expiryAt": "2024-09-24T10:00:00Z",
     *     "status": "ACTIVE"
     * }
     * </pre>
     * </p>
     *
     * @param commandType the type of command being executed, must be {@code COMMAND_POST_ANONYMOUS_TOKEN}
     * @return a {@link ResponseEntity} containing the generated token details
     * @throws GlobalException if an error occurs
     */
    @Operation(
            description = """
            ### Generate Token by Anonymous user
            Use this endpoint to generate a token for an user based in not access.
            - The commandType must be COMMAND_POST_ANONYMOUS_TOKEN.

            **Example Path JSON:**
            ```json
            {
                path: "/auth/v1/post/anonymous/token/COMMAND_POST_ANONYMOUS_TOKEN"
            }
            ```
            
            **Example Response Body JSON:**
            ```json
            {
                "token": "Bearer <your-anonymous-access-token>",
                "createAt": "2024-09-23T10:00:00Z",
                "expiryAt": "2024-09-24T10:00:00Z",
                "status": "ACTIVE"
            }

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "201", description = "Token generated successfully", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Token.class))
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
    @PostMapping(path = Router.ROUTER_POST_ANONYMOUS_TOKEN + "/{commandType}", produces = "application/json")
    ResponseEntity<Token> postAnonymousToken(
            @PathVariable(name = Request.COMMAND_TYPE) String commandType
    ) throws GlobalException;
}
