package com.pi.core_auth.ports.in;

import com.pi.core_auth.core.domains.Token;
import com.pi.core_auth.core.utils.constants.Request;
import com.pi.core_auth.core.utils.constants.Router;
import com.pi.utils.exceptions.GlobalException;

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

/**
 * Interface for handling authentication-related queries.
 * <p>
 * This interface defines the contract for querying authentication-related
 * operations, such as validating the status of a token.
 * </p>
 */
@Tag(name = "Auth Query: port In")
public interface IAuthQueryIn {

    /**
     * Validates the status of a token.
     * <p>
     * This endpoint is used to validate the status of a token. The query type
     * must be {@code GET_STATUS_TOKEN}, and the token must be valid.
     * </p>
     * <p>
     * Example Path JSON:
     * <pre>
     * {
     *     "path": "/auth/v1/get/status/token/GET_STATUS_TOKEN"
     * }
     * </pre>
     * Example Headers JSON:
     * <pre>
     * {
     *     "Authorization": "Bearer &lt;your-token&gt;"
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
     * @param queryType the type of query being performed, must be {@code GET_STATUS_TOKEN}
     * @param authorization the authorization header containing the bearer token
     * @return a {@link ResponseEntity} containing the token details if valid
     * @throws GlobalException if the token is invalid or an error occurs
     */
    @Operation(
            security = @SecurityRequirement(name = "bearer-key"),
            description = """
            ### Validate status token
            Use this endpoint to validate the status of a token.
            - The queryType must be GET_STATUS_TOKEN.
            - The token must be a valid token.

            **Example Path JSON:**
            ```json
            {
                path: "/auth/v1/get/status/token/GET_STATUS_TOKEN"
            }
            ```
            
            **Example Headers JSON:**
            ```json
            {
                "Authorization": "Bearer <your-token>",
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
                    @ApiResponse(responseCode = "200", description = "Token generated successfully", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Token.class))
                    }),
                    @ApiResponse(responseCode = "400 to 500", description = "Error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalException.class))
                    })
            }
    )
    @GetMapping(path = Router.GET_STATUS_TOKEN + "/{queryType}", produces = "application/json")
    @PreAuthorize("hasRole('ROLE_ANONYMOUS') and hasAuthority('SCOPE_READ')")
    ResponseEntity<Token> getStatusToken(
            @PathVariable(name = Request.QUERY_TYPE) String queryType,
            @RequestHeader(name = Request.AUTHORIZATION) String authorization
    ) throws GlobalException;
}
