package com.pi.core_auth.core.utils.services;

import com.pi.core_auth.core.enums.StatusType;
import com.pi.core_auth.core.utils.constants.Response;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.JwtException;

public class Utils {
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

    /**
     * Removes the "Bearer " prefix from the token if present.
     *
     * @param token the JWT token string
     * @return the token without the "Bearer " prefix
     */
    public static String removePrefixBearerToToken(String token) {
        if (token.startsWith("Bearer ")) {
            LOG.info("Removing 'Bearer ' prefix from token: {}", token);
            return token.substring(7);
        }
        return token;
    }

    /**
     * Validates the JWT token and handles exceptions.
     *
     * @param exception the JwtException thrown during token validation
     * @throws GlobalException if the token is invalid or expired
     */
    public static Response validateResponseJwtException(JwtException exception) {
        LOG.warn("Error decoding token: {}", exception.getMessage());
        if (exception.getMessage().contains("Expired")) {
            LOG.info("Token is expired.");
            return Response.builder()
                    .status(StatusType.EXPIRED)
                    .build();
        } else if (exception.getMessage().contains("Invalid")) {
            LOG.info("Token is invalid.");
            return Response.builder()
                    .status(StatusType.INVALID)
                    .build();
        } else {
            LOG.error("Token is invalid.");
            throw GlobalException.builder()
                    .status(422)
                    .alert(new CustomAlert(SystemCodeEnum.C060PI))
                    .build();
        }
    }
}
