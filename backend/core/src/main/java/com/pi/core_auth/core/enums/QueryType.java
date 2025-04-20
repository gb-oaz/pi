package com.pi.core_auth.core.enums;

import java.util.EnumSet;
import java.util.Set;

/**
 * Enum representing the types of queries that can be performed in the system.
 * Each query type includes a description and the roles permitted to execute it.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * QueryType query = QueryType.GET_STATUS_TOKEN;
 * System.out.println("Query: " + query.name());
 * System.out.println("Description: " + query.getDescription());
 * System.out.println("Permissions: " + query.getPermissions());
 * }</pre>
 *
 * @author Gustavo
 * @since 1.0
 */
public enum QueryType {
    /**
     * Query to retrieve the status of a token.
     * Suggested for use in scenarios where token validation or status checks are required.
     */
    QUERY_GET_STATUS_TOKEN(
        "Retrieve the status of a token. Use this query to validate or check the status of a token.",
        EnumSet.of(ScopeType.ANONYMOUS, ScopeType.STUDENT, ScopeType.TEACHER)
    ),

    /**
     * Query to retrieve the permissions associated with a token.
     * Suggested for use in scenarios where permission checks are required.
     */
    QUERY_GET_SCOPE_TOKEN(
        "Retrieve the scope of a token. Use this query to check the permissions associated with a token.",
        EnumSet.of(ScopeType.ANONYMOUS, ScopeType.STUDENT, ScopeType.TEACHER)
    );

    private final String description;
    private final Set<ScopeType> permissions;

    /**
     * Constructor for the {@code QueryType} enum.
     *
     * @param description A brief description of the query type.
     * @param permissions The roles permitted to execute this query.
     */
    QueryType(String description, Set<ScopeType> permissions) {
        this.description = description;
        this.permissions = permissions;
    }

    /**
     * Returns the description of the query type.
     *
     * @return A string representing the description of the query type.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the roles permitted to execute this query type.
     *
     * @return A set of {@code RoleType} representing the permitted roles.
     */
    public Set<ScopeType> getPermissions() {
        return permissions;
    }
}
