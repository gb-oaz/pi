package com.pi.core_auth.usecases;

import com.pi.core_auth.core.domains.Token;
import com.pi.core_auth.core.dtos.CommandDto;
import com.pi.core_auth.core.enums.ScopeType;
import com.pi.core_auth.core.enums.StatusType;
import com.pi.core_auth.core.utils.constants.Claim;
import com.pi.core_auth.ports.out.IAuthCommandOut;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Service class for handling the generation of sign-in tokens.
 * <p>
 * This class implements the {@link Callable} interface to generate a JWT token
 * for a user based on their login credentials. It validates the user, builds
 * the token claims, and encodes the token.
 * </p>
 */
@Service
public class CasePostSignInToken implements Callable<Token> {
    private static final Logger LOG = LoggerFactory.getLogger(CasePostSignInToken.class);

    /**
     * Expiry time for the token in seconds (approximately 1 month).
     */
    @Value("${jwt.expiry-seconds:2629344}") Long EXPIRY_SECONDS;

    /**
     * Identifier for the JWT issuer.
     */
    @Value("${jwt.issuer:ms_auth}") String MS_AUTH = "ms_auth";

    /**
     * JWT encoder used to encode the token.
     */
    private final JwtEncoder encoder;

    /**
     * Output port for authentication-related commands.
     */
    private IAuthCommandOut authCommandOut;

    /**
     * Data transfer object containing the command details.
     */
    private CommandDto commandDto;

    /**
     * Constructs a {@code CasePostSignInToken} instance with a provided {@link JwtEncoder}.
     *
     * @param encoder the {@link JwtEncoder} used to encode JWT tokens.
     */
    @Autowired
    public CasePostSignInToken(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    /**
     * Sets the authentication command output service.
     *
     * @param authCommandOut the output port for authentication-related commands.
     */
    public void setServices(IAuthCommandOut authCommandOut) {
        this.authCommandOut = authCommandOut;
    }

    /**
     * Sets the command data transfer object.
     *
     * @param commandDto the {@link CommandDto} containing the command details.
     */
    public void setCommandDto(CommandDto commandDto) {
        this.commandDto = Objects.requireNonNull(commandDto, "CommandDto cannot be null");
    }

    /**
     * Executes the token generation process.
     *
     * @return the generated {@link Token}.
     * @throws GlobalException if validation fails or an error occurs.
     */
    @Override
    public Token call() throws GlobalException {
        LOG.info("Init CasePostSignInToken call.");
        commandDto.validate();
        var scopes = findRolesUserIfExist();
        var claims = buildClaims(scopes);
        var token = generateToken(claims);
        LOG.info("End CasePostSignInToken call.");
        return token;
    }

    /**
     * Finds the scopes of a user if they exist.
     *
     * @return an {@link EnumSet} of {@link ScopeType} representing the user's scopes.
     * @throws GlobalException if the user does not exist or an error occurs.
     */
    protected EnumSet<ScopeType> findRolesUserIfExist() throws GlobalException {
        LOG.info("Init Find roles user if exist.");
        var scopes = authCommandOut.existUser(commandDto.login(), commandDto.code(), commandDto.password());
        if (scopes.isEmpty()) {
            LOG.warn("{} - User not found.", SystemCodeEnum.C050PI.name());
            throw GlobalException.builder()
                    .status(401)
                    .alert(new CustomAlert(SystemCodeEnum.C050PI))
                    .build();
        }
        LOG.info("End Find roles user if exist.");
        return scopes;
    }

    /**
     * Builds the JWT claims for the token.
     *
     * @param scopes the scopes of the user.
     * @return the {@link JwtClaimsSet} containing the token claims.
     */
    protected JwtClaimsSet buildClaims(EnumSet<ScopeType> scopes) {
        LOG.info("Init Build claims for token.");
        var now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer(MS_AUTH)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(EXPIRY_SECONDS))
                .subject(commandDto.login())
                .claim(Claim.SCOPE, getAuthoritiesScopes(scopes).stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" ")))
                .build();
    }

    /**
     * Generates the JWT token based on the provided claims.
     *
     * @param claims the {@link JwtClaimsSet} containing the token claims.
     * @return the generated {@link Token}.
     * @throws GlobalException if an error occurs during token generation.
     */
    protected Token generateToken(JwtClaimsSet claims) {
        try {
            LOG.info("Init Generate token.");
            var now = Instant.now();
            var jwtToken = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            return Token.builder()
                    .token(jwtToken)
                    .createAt(now.toString())
                    .expiryAt(now.plusSeconds(EXPIRY_SECONDS).toString())
                    .status(StatusType.ACTIVE)
                    .build();
        } catch (Exception e) {
            LOG.error("Error generating token: {}", e.getMessage());
            throw GlobalException.builder()
                    .status(401)
                    .alert(new CustomAlert(SystemCodeEnum.C050PI))
                    .build();
        }
    }

    /**
     * Retrieves the authorities for the user's scopes.
     *
     * @param scopes the scopes of the user.
     * @return a {@link Set} of {@link GrantedAuthority} representing the user's scopes.
     */
    protected Set<GrantedAuthority> getAuthoritiesScopes(EnumSet<ScopeType> scopes) {
        LOG.info("Get authorities scopes for user session.");
        return scopes.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
    }
}