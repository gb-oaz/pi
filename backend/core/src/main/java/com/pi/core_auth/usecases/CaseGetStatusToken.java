package com.pi.core_auth.usecases;

import com.pi.core_auth.core.domains.Token;
import com.pi.core_auth.core.dtos.QueryDto;
import com.pi.core_auth.core.enums.StatusType;
import com.pi.core_auth.core.utils.constants.Response;
import com.pi.core_auth.core.utils.services.Utils;
import com.pi.utils.exceptions.GlobalException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Service class for handling token status verification.
 * <p>
 *      This class implements the {@link Callable} interface to verify
 *      and return the status of a JWT token.
 * </p>
 */
@Service
public class CaseGetStatusToken implements Callable<Response> {
    /**
     * JWT decoder used to decode token.
     */
    private final JwtDecoder jwtDecoder;

    /**
     * Data transfer object containing the query details.
     */
    private QueryDto queryDto;

    /**
     * Constructs a {@code CaseGetStatusToken} instance with a provided {@link JwtDecoder}.
     *
     * @param jwtDecoder the {@link JwtDecoder} used to decode JWT tokens.
     */
    @Autowired
    public CaseGetStatusToken(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    /**
     * Sets the query data transfer object.
     *
     * @param queryDto the {@link QueryDto} containing the query details.
     */
    public void setQueryDto(QueryDto queryDto) {
        this.queryDto = Objects.requireNonNull(queryDto, "QueryDto cannot be null");
    }

    /**
     * Validates the token and returns its status.
     *
     * @return a {@link Token} object containing the token and its status.
     * @throws GlobalException if an error occurs during token validation.
     */
    @Override
    public Response call() throws GlobalException {
        var token = queryDto.validate();
        var onlyToken = Utils.removePrefixBearerToToken(token.getToken());
        return validateTokenStatus(onlyToken);
    }

    /**
     * Validates the JWT token and returns a Token object with the appropriate status.
     *
     * @param token the JWT token string to validate
     * @return a Token object containing the validation result
     */
    protected Response validateTokenStatus(String token) {
        try {
            jwtDecoder.decode(token);
            return Response.builder()
                .status(StatusType.ACTIVE)
                .build();
        } catch (JwtException e) {
            return Response.builder()
                .status(StatusType.EXPIRED)
                .build();
        }
    }
}
