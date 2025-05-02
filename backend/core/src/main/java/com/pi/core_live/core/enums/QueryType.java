package com.pi.core_live.core.enums;


import com.pi.core_auth.core.enums.ScopeType;

import java.util.EnumSet;
import java.util.Set;

/**
 * Enum representing the types of queries that can be performed in the system.
 * Each query type includes a description and the roles permitted to execute it.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * QueryType query = QueryType.GET_LIVE;
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
     * Query to retrieve a live.
     *
     * @since 1.0
     */
    QUERY_GET_LIVE("Retrieve a live.", EnumSet.of(ScopeType.ANONYMOUS, ScopeType.STUDENT, ScopeType.TEACHER)),

    /**
     * Query to retrieve a live stream.
     *
     * @since 1.0
     */
    QUERY_GET_LIVE_STREAM("Retrieve a live stream.", EnumSet.of(ScopeType.ANONYMOUS, ScopeType.STUDENT, ScopeType.TEACHER));

    private final String description;
    private final Set<ScopeType> permissions;

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
     * @return A set of {@code ScopeType} representing the permitted roles.
     */
    public Set<ScopeType> getPermissions() {
        return permissions;
    }
}
