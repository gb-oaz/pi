package com.pi.core_auth.core.domains;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pi.core_auth.core.enums.StatusType;

/**
 * The {@code Token} class represents a token used in authentication,
 * containing the token string, creation time, and expiration time.
 * It provides a builder-like pattern for constructing instances.
 * <p>
 * This class is designed to handle JWT tokens, where the {@code createAt} and {@code expiryAt}
 * parameters are set at the moment of token creation.
 * </p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * Token token = Token.builder()
 *     .token("Bearer <your-access-token>")
 *     .createAt("2024-09-23T10:00:00Z")
 *     .expiryAt("2024-09-24T10:00:00Z")
 *     .status(StatusType.ACTIVE)
 *     .build();
 *
 * System.out.println("Token: " + token.getToken());
 * System.out.println("Created At: " + token.getCreateAt());
 * System.out.println("Expires At: " + token.getExpiryAt());
 * System.out.println("Status: " + token.getStatus());
 * }</pre>
 *
 * <p>
 * The builder pattern allows for a more readable and maintainable way to construct
 * {@code Token} instances, especially when dealing with multiple parameters.
 * </p>
 *
 * @author GustavoBoaz
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Token {
    private String token;
    private String createAt;
    private String expiryAt;
    private StatusType status;

    /**
     * Default constructor for the {@code Token} class.
     * Initializes an empty {@code Token} object.
     */
    public Token() { }

    /**
     * Returns a new instance of {@code Token} to be used with the builder pattern.
     *
     * @return a new {@code Token} instance.
     */
    public static Token builder() { return new Token(); }

    public Token token(String token) { this.token = token; return this; }
    public Token createAt(String createAt) { this.createAt = createAt; return this; }
    public Token expiryAt(String expiryAt) { this.expiryAt = expiryAt; return this; }
    public Token status(StatusType status) { this.status = status; return this; }

    /**
     * Finalizes the {@code Token} construction process.
     *
     * @return the current {@code Token} instance.
     */
    public Token build() { return this; }

    // Getters
    public String getToken() { return this.token; }
    public String getCreateAt() { return this.createAt; }
    public String getExpiryAt() { return this.expiryAt; }
    public StatusType getStatus() { return this.status; }
}
