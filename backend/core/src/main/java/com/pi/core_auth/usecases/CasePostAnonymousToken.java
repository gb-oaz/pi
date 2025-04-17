package com.pi.core_auth.usecases;

import com.pi.core_auth.core.domains.Token;
import com.pi.core_auth.core.dtos.CommandDto;
import com.pi.core_auth.core.enums.ScopeType;
import com.pi.core_auth.core.enums.StatusType;
import com.pi.core_auth.core.utils.constants.Claim;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Service class for handling the generation of anonymous tokens.
 * <p>
 * This class implements the {@link Callable} interface to generate a JWT token
 * for a user based on their login credentials. It validates the user, builds
 * the token claims, and encodes the token.
 * </p>
 */
@Service
public class CasePostAnonymousToken implements Callable<Token> {
    /**
     * Expiry time for the token in seconds (approximately 1 day).
     */
    @Value("${jwt.expiry-seconds:86400}") private Long EXPIRY_SECONDS;

    /**
     * Identifier for the JWT issuer.
     */
    @Value("${jwt.issuer:ms_auth}") private String MS_AUTH;

    /**
     * Prefix for the subject of the JWT token.
     */
    private static final String PUPIL = "PUPIL-";

    /**
     * SecureRandom instance for generating random numbers.
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * JWT encoder used to encode the token.
     */
    private final JwtEncoder encoder;

    /**
     * Data transfer object containing the command details.
     */
    private CommandDto commandDto;

    /**
     * Constructs a {@code CasePostAnonymousToken} instance with a provided {@link JwtEncoder}.
     *
     * @param encoder the {@link JwtEncoder} used to encode JWT tokens.
     */
    @Autowired
    public CasePostAnonymousToken(JwtEncoder encoder) {
        this.encoder = encoder;
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
        commandDto.validate();
        var claims = buildClaims();
        return generateToken(claims);
    }

    /**
     * Builds the JWT claims for the token.
     *
     * @return the {@link JwtClaimsSet} containing the token claims.
     */
    protected JwtClaimsSet buildClaims() {
        var now = Instant.now();
        return JwtClaimsSet.builder()
                .issuer(MS_AUTH)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(EXPIRY_SECONDS))
                .subject(PUPIL + generateToken())
                .claim(Claim.SCOPE, getAuthoritiesScopes().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" ")))
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
            var now = Instant.now();
            var jwtToken = encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            return Token.builder()
                    .token(jwtToken)
                    .createAt(now.toString())
                    .expiryAt(now.plusSeconds(EXPIRY_SECONDS).toString())
                    .status(StatusType.ACTIVE)
                    .build();
        } catch (Exception e) {
            throw GlobalException.builder()
                    .status(500)
                    .alert(new CustomAlert(SystemCodeEnum.C050PI))
                    .build();
        }
    }

    /**
     * Retrieves the authorities for the user's scopes.
     *
     * @return a {@link Set} of {@link GrantedAuthority} representing the user's scopes.
     */
    protected Set<GrantedAuthority> getAuthoritiesScopes() {
        var scopes = EnumSet.of(ScopeType.ANONYMOUS);
        return scopes.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
    }

    /**
     * Generates a token using the current timestamp and a random number.
     *
     * @return the generated token. ex - 17f9343c5c114ed6a
     */
    protected static String generateToken() {
        long currentTimeMillis = System.currentTimeMillis();
        int randomInt = SECURE_RANDOM.nextInt();
        return Long.toHexString(currentTimeMillis) + Integer.toHexString(randomInt);
    }
}