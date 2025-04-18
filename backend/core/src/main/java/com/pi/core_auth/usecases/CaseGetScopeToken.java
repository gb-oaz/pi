package com.pi.core_auth.usecases;

import com.pi.core_auth.core.dtos.QueryDto;
import com.pi.core_auth.core.enums.ScopeType;
import com.pi.core_auth.core.utils.constants.Claim;
import com.pi.core_auth.core.utils.constants.Response;
import com.pi.core_auth.core.utils.services.Utils;
import com.pi.utils.exceptions.GlobalException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Service class for handling token scope retrieval.
 * <p>
 *     This class implements the {@link Callable} interface to verify
 *     and retrieving the scope of a JWT token.
 * </p>
 */
@Service
public class CaseGetScopeToken implements Callable<Response> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseGetScopeToken.class);

    /**
     * JWT decoder used to decode token.
     */
    private final JwtDecoder jwtDecoder;

    /**
     * Data transfer object containing the query details.
     */
    private QueryDto queryDto;

    /**
     * Constructs a {@code CaseGetScopeToken} instance with a provided {@link JwtDecoder}.
     *
     * @param jwtDecoder the {@link JwtDecoder} used to decode JWT tokens.
     */
    public CaseGetScopeToken(JwtDecoder jwtDecoder) {
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
     * Validates the token and retrieves its scope.
     *
     * @return a {@link Response} object containing the scope of the token.
     * @throws GlobalException if an error occurs during token validation.
     */
    @Override
    public Response call() throws GlobalException {
        LOG.info("Init CaseGetScopeToken call.");
        var token = queryDto.validate();
        var onlyToken = Utils.removePrefixBearerToToken(token.getToken());
        var scope = getScopeToken(onlyToken);
        LOG.info("End CaseGetScopeToken call.");
        return scope;
    }

    /**
     * Retrieves the scope of a JWT token.
     *
     * @param token the JWT token string to retrieve the scope from.
     * @return a {@link Response} object containing the scope of the token.
     */
    protected Response getScopeToken(String token) {
        LOG.info("Init Get scope token from token: {}", token);
        var jwt = jwtDecoder.decode(token);
        var scopes = jwt.getClaimAsStringList(Claim.SCOPE);
        var scopeSet = convertToEnumSet(scopes);
        LOG.info("End Get scope token from token: {}", token);
        return Response.builder()
                .scope(scopeSet)
                .build();
    }

    /**
     * Converts a list of scope strings to an {@link EnumSet} of {@link ScopeType}.
     *
     * @param scopes the list of scope strings to convert.
     * @return an {@link EnumSet} of {@link ScopeType} representing the converted scopes.
     */
    protected EnumSet<ScopeType> convertToEnumSet(List<String> scopes) {
        LOG.info("Convert To EnumSet with scopes: {}", scopes);
        if (CollectionUtils.isEmpty(scopes)) return EnumSet.noneOf(ScopeType.class);

        return scopes.stream()
                .map(ScopeType::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(ScopeType.class)));
    }
}
