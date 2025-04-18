package com.pi.core_auth.core.utils.services;

import com.pi.core_auth.usecases.CaseGetScopeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
