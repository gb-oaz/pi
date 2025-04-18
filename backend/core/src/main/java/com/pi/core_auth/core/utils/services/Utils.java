package com.pi.core_auth.core.utils.services;

public class Utils {
    /**
     * Removes the "Bearer " prefix from the token if present.
     *
     * @param token the JWT token string
     * @return the token without the "Bearer " prefix
     */
    public static String removePrefixBearerToToken(String token) {
        if (token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
