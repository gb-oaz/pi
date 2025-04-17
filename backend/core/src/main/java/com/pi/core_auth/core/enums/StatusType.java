package com.pi.core_auth.core.enums;

/**
 * Enum representing the status types of a token in the system.
 * Each status type includes a description explaining its purpose.
 *
 * <p>Example usage with the {@code Token} class:</p>
 * <pre>{@code
 * Token token = Token.builder()
 *     .token("abc123")
 *     .createAt("2024-09-23T10:00:00Z")
 *     .expiryAt("2024-09-24T10:00:00Z")
 *     .status(StatusType.ACTIVE)
 *     .build();
 *
 * System.out.println("Token Status: " + token.getStatus().name());
 * }</pre>
 *
 * @author Gustavo
 * @since 1.0
 */
public enum StatusType {
    /**
     * Indicates that the token is active and can be used without restrictions.
     */
    ACTIVE("The token is active and can freely navigate through the system."),

    /**
     * Indicates that the token is expired and cannot be used.
     */
    EXPIRED("The token is expired and cannot be used."),;

    private final String description;

    /**
     * Constructor for the {@code StatusType} enum.
     *
     * @param description A brief description of the status type.
     */
    StatusType(String description) {
        this.description = description;
    }

    /**
     * Returns the description of the status type.
     *
     * @return A string representing the description of the status type.
     */
    public String getDescription() {
        return description;
    }
}
