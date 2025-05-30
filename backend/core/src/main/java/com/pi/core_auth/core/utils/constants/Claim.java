package com.pi.core_auth.core.utils.constants;

/**
 * The {@code Claim} class defines constant values representing the keys used in JWT claims.
 * These claims are used to store and retrieve important information in the token payload,
 * ensuring consistency in how the data is structured across the authentication system.
 */
public class Claim {
    /**
     * The claim key representing the scope or permissions granted to the token bearer.
     * Typically used to define the access level or actions allowed for the token.
     */
    public static final String SCOPE = "scope";

    /**
     * The claim key representing the username or identifier of the token bearer.
     * Typically used to identify the user associated with the token.
     */
    public static final String LOGIN = "login";

    /**
     * The claim key representing the user's code.
     * Used to verify the identity of the user associated with the token.
     */
    public static final String CODE = "code";
}
