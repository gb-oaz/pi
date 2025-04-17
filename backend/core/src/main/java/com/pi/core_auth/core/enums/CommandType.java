package com.pi.core_auth.core.enums;

import java.util.EnumSet;
import java.util.Set;

/**
 * Enum representing the types of commands that can be executed in the system.
 * Each command type includes a description and the roles permitted to execute it.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * CommandType command = CommandType.POST_SIGN_IN_TOKEN;
 * System.out.println("Command: " + command.name());
 * System.out.println("Description: " + command.getDescription());
 * System.out.println("Permissions: " + command.getPermissions());
 * }</pre>
 *
 * @since 1.0
 */
public enum CommandType {
    /**
     * Command to post a sign-in token.
     * Suggested for use in scenarios where user authentication is required.
     */
    POST_SIGN_IN_TOKEN(
        "Post a sign-in token. Use this command to authenticate users.",
        EnumSet.of(ScopeType.ANONYMOUS)
    ),

    /**
     * Command to post an anonymous token.
     * Suggested for use in scenarios where guest access is required.
     */
    POST_ANONYMOUS_TOKEN(
        "Post an anonymous token. Use this command to provide guest access.",
        EnumSet.of(ScopeType.ANONYMOUS)
    );

    private final String description;
    private final Set<ScopeType> permissions;

    /**
     * Constructor for the {@code CommandType} enum.
     *
     * @param description A brief description of the command type.
     * @param permissions The roles permitted to execute this command.
     */
    CommandType(String description, Set<ScopeType> permissions) {
        this.description = description;
        this.permissions = permissions;
    }

    /**
     * Returns the description of the command type.
     *
     * @return A string representing the description of the command type.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the roles permitted to execute this command type.
     *
     * @return A set of {@code RoleType} representing the permitted roles.
     */
    public Set<ScopeType> getPermissions() {
        return permissions;
    }
}
