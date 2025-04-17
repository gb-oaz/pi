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
 */
@Service
public class CasePostAnonymousToken implements Callable<Token> {

    public static final String PUPIL = "PUPIL-";

    @Value("${jwt.expiry-seconds:86400}") // Default to 1 day
    private Long EXPIRY_SECONDS;

    @Value("${jwt.issuer:ms_auth}")
    private String MS_AUTH;

    private final JwtEncoder encoder;

    private CommandDto commandDto;

    private static final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    public CasePostAnonymousToken(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public void setCommandDto(CommandDto commandDto) {
        this.commandDto = Objects.requireNonNull(commandDto, "CommandDto cannot be null");
    }

    @Override
    public Token call() throws GlobalException {
        commandDto.validate();
        var claims = buildClaims();
        return generateToken(claims);
    }

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
        int randomInt = secureRandom.nextInt();
        return Long.toHexString(currentTimeMillis) + Integer.toHexString(randomInt);
    }
}