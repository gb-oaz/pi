package com.pi.core_user.ports.in;

import com.pi.core_user.core.domains.User;
import com.pi.core_user.core.utils.constants.Router;
import com.pi.utils.constants.Command;
import com.pi.utils.constants.Request;
import com.pi.utils.exceptions.GlobalException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Interface for handling user-related commands.
 * <p>
 *     This interface defines the contract for executing user-related operations,
 *     such as creating, updating, or deleting users.
 * </p>
 */
@Tag(name = "User Commands: port In")
public interface IUserCommandIn {

    /**
     * Create a new teacher user based on register data.
     * <p>
     * This endpoint is used to create a new teacher user in the system.
     * The command type must be {@code COMMAND_POST_CREATE_USER_TEACHER}.
     * </p>
     * <p>
     * Example Path JSON:
     * <pre>
     * {
     *     "path": "/user/v1/post/create/user/teacher/COMMAND_POST_CREATE_USER_TEACHER"
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
     *     "name": "Gustavo Boaz",
     *     "email": "gustavo.boaz@domainpi.com",
     *     "login": "GUSTAVOBOAZ",
     *     "code": "123456",
     *     "password": "Password@123456"
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
     * @param commandType the type of command being executed, must be {@code COMMAND_POST_CREATE_USER_TEACHER}
     * @param anonymousToken the authorization header containing the bearer token
     * @param name the user's name, must be between 3 and 40 characters with space
     * @param email the user's email, must be a valid email address
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
            - The commandType must be COMMAND_POST_CREATE_USER_TEACHER.
            - The name must be between 3 and 40 characters with space.
            - The email must be a valid email address.
            - The login must be at least 8 characters uppercase letters.
            - The code must be at least 6 numbers.
            - The password must be at least 15 characters long, containing at least one lowercase letter,
                one uppercase letter, one digit, and one special character !?@#$%&.
            - The anonymousToken must be a valid token.
            
            **Example Path JSON:**
            ```json
            {
                "path": "/user/v1/post/create/user/teacher/COMMAND_POST_CREATE_USER_TEACHER"
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
                "name": "Gustavo Boaz",
                "email": "gustavo.boaz@domainpi.com",
                "login": "GUSTAVOBOAZ",
                "code": "123456",
                "password": "Password@123456",
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
                    @ApiResponse(responseCode = "201", description = "User created successfully", content = {
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
    @PostMapping(path = Router.ROUTER_POST_CREATE_USER_TEACHER + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_ANONYMOUS')")
    ResponseEntity<User> postCreateUserTeacher(
        @PathVariable(Request.COMMAND_TYPE) String commandType,
        @RequestHeader(Request.AUTHORIZATION) String anonymousToken,
        @RequestPart(Command.NAME) String name,
        @RequestPart(Command.EMAIL) String email,
        @RequestPart(Command.LOGIN) String login,
        @RequestPart(Command.CODE) String code,
        @RequestPart(Command.PASSWORD) String password
    ) throws GlobalException;

    /**
     * Create a new student user based on register data.
     * <p>
     * This endpoint is used to create a new student user in the system.
     * The command type must be {@code POST_CREATE_USER_TEACHER}.
     * </p>
     * <p>
     * Example Path JSON:
     * <pre>
     * {
     *     "path": "/user/v1/post/create/user/student/COMMAND_POST_CREATE_USER_STUDENT"
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
     *     "name": "Gustavo Boaz",
     *     "email": "gustavo.boaz@domainpi.com",
     *     "login": "GUSTAVOBOAZ",
     *     "code": "123456",
     *     "password": "Password@123456"
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
     * @param commandType the type of command being executed, must be {@code COMMAND_POST_CREATE_USER_STUDENT}
     * @param anonymousToken the authorization header containing the bearer token
     * @param name the user's name, must be between 3 and 40 characters with space
     * @param email the user's email, must be a valid email address
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
            - The commandType must be COMMAND_POST_CREATE_USER_STUDENT.
            - The name must be between 3 and 40 characters with space.
            - The email must be a valid email address.
            - The login must be at least 8 characters uppercase letters.
            - The code must be at least 6 numbers.
            - The password must be at least 15 characters long, containing at least one lowercase letter,
                one uppercase letter, one digit, and one special character !?@#$%&.
            - The anonymousToken must be a valid token.
            
            **Example Path JSON:**
            ```json
            {
                "path": "/user/v1/post/create/user/student/COMMAND_POST_CREATE_USER_STUDENT"
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
                "name": "Gustavo Boaz",
                "email": "gustavo.boaz@domainpi.com",
                "login": "GUSTAVOBOAZ",
                "code": "123456",
                "password": "Password@123456",
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
                    @ApiResponse(responseCode = "201", description = "User created successfully", content = {
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
    @PostMapping(path = Router.ROUTER_POST_CREATE_USER_STUDENT + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_ANONYMOUS')")
    ResponseEntity<User> postCreateUserStudent(
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String anonymousToken,
            @RequestPart(Command.NAME) String name,
            @RequestPart(Command.EMAIL) String email,
            @RequestPart(Command.LOGIN) String login,
            @RequestPart(Command.CODE) String code,
            @RequestPart(Command.PASSWORD) String password
    ) throws GlobalException;

    /**
     * Update user based on register data.
     * <p>
     * This endpoint is used to create a new student user in the system.
     * The command type must be {@code COMMAND_PUT_UPDATE_USER}.
     * </p>
     * <p>
     * Example Path JSON:
     * <pre>
     * {
     *     "path": "/user/v1/put/update/user/COMMAND_PUT_UPDATE_USER"
     * }
     * </pre>
     * Example Headers JSON:
     * <pre>
     * {
     *     "Authorization": "Bearer &lt;token&gt;"
     * }
     * </pre>
     * Example Request Body Parts JSON:
     * <pre>
     * {
     *     "name": "Gustavo Boaz",
     *     "email": "gustavo.boaz@domainpi.com",
     *     "password": "Password@123456"
     *     "oldPassword": "Password@123456"
     * }
     * </pre>
     * Example Response Body JSON:
     * <pre>
     * {
     *     "name": "Gustavo Boaz",
     *     "email": "gustavo.boaz@domainpi.com",
     *     "createAt": "2023-08-01T00:00:00.000Z",
     *     "updateAt": "2023-08-01T00:00:00.000Z"
     * }
     * </pre>
     * </p>
     *
     * @param commandType the type of command being executed, must be {@code COMMAND_PUT_UPDATE_USER}
     * @param token the authorization header containing the bearer token
     * @param name the user's name, must be between 3 and 40 characters with space
     * @param email the user's email, must be a valid email address
     * @param password the user's password, must be at least 15 characters long with specific requirements
     * @param oldPassword the user's old password
     * @return a {@link ResponseEntity} containing the generated token details
     * @throws GlobalException if validation fails or an error occurs
     */
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Generate Token by Sign In
            Use this endpoint to generate a token for an user based on login credentials.
            - The commandType must be COMMAND_PUT_UPDATE_USER.
            - The token must be a valid token.
            - The name must be between 3 and 40 characters with space.
            - The email must be a valid email address.
            - The password must be at least 15 characters long, containing at least one lowercase letter,
                one uppercase letter, one digit, and one special character !?@#$%&.
            
            **Example Path JSON:**
            ```json
            {
                "path": "/user/v1/put/update/user/COMMAND_PUT_UPDATE_USER"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <token>",
            }
            ```

            **Example Request Body Parts JSON:**
            ```json
            {
                "name": "Gustavo Boaz",
                "email": "gustavo.boaz@domainpi.com",
                "password": "Password@123456",
                "oldPassword": "Password@123456"
            }
            ```
            
            **Example Response Body JSON:**
            ```json
            {
                "name": "Gustavo Boaz",
                "email": "gustavo.boaz@domainpi.com",
                "createAt": "2024-09-23T10:00:00Z",
                "updateAt": "2024-09-23T10:00:00Z"
            }

            For more details, contact the developer team.
            """,
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully", content = {
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
    @PutMapping(path = Router.ROUTER_PUT_UPDATE_USER + "/{commandType}", produces = "application/json", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyAuthority('SCOPE_TEACHER', 'SCOPE_STUDENT')")
    ResponseEntity<User> putUpdateUser(
            @PathVariable(Request.COMMAND_TYPE) String commandType,
            @RequestHeader(Request.AUTHORIZATION) String token,
            @RequestPart(value = Command.NAME, required = false) String name,
            @RequestPart(value = Command.EMAIL, required = false) String email,
            @RequestPart(value = Command.PASSWORD, required = false) String password,
            @RequestPart(value = Command.OLD_PASSWORD, required = false) String oldPassword
    ) throws GlobalException;
}
