package com.pi.core_live.core.enums;

import com.pi.core_auth.core.enums.ScopeType;

import java.util.EnumSet;
import java.util.Set;

/**
 * Enum representing the types of command that can be executed in the system.
 * Each command type includes a description and the roles permitted to execute it.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * QueryType query = QueryType.COMMAND_POST_NEW_LIVE;
 * System.out.println("Query: " + query.name());
 * System.out.println("Description: " + query.getDescription());
 * System.out.println("Permissions: " + query.getPermissions());
 * }</pre>
 *
 * @since 1.0
 */
public enum CommandType {
    /**
     * Command to create a new live.
     */
    COMMAND_POST_NEW_LIVE(
            "Post a new live. use this command to create a new live.",
            EnumSet.of(ScopeType.TEACHER)
    ),

    /**
     * Command to update for next position of a live.
     */
    COMMAND_PATCH_NEXT_POSITION(
            "Patch the next position of the teacher. use this command to move the teacher to the next position.",
            EnumSet.of(ScopeType.TEACHER)
    ),

    /**
     * Command to update for previous position of a live.
     */
    COMMAND_PATCH_PREVIOUS_POSITION(
            "Patch the previous position of the teacher. use this command to move the teacher to the previous position.",
            EnumSet.of(ScopeType.TEACHER)
    ),

    /**
     * Command to update for remove a pupil from the lobby.
     */
    COMMAND_PATCH_REMOVE_PUPIL_FROM_LOBBY(
            "Patch the remove a pupil from the lobby. use this command to remove a pupil from the lobby.",
            EnumSet.of(ScopeType.TEACHER)
    ),

    /**
     * Command to update for add a pupil to the lobby.
     */
    COMMAND_PATCH_ADD_PUPIL_TO_LOBBY(
            "Patch the add a pupil to the lobby. use this command to add a pupil to the lobby.",
            EnumSet.of(ScopeType.ANONYMOUS, ScopeType.TEACHER, ScopeType.STUDENT)
    ),

    /**
     * Command to update for add a pupil answer to the quiz.
     */
    COMMAND_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ(
            "Patch the add a pupil answer to the quiz. use this command to add a pupil answer to the quiz.",
            EnumSet.of(ScopeType.ANONYMOUS, ScopeType.TEACHER, ScopeType.STUDENT)
    ),

    /**
     * Command to update for end a live.
     */
    COMMAND_PATCH_END_LIVE(
            "Patch the end live. use this command to end the live.",
            EnumSet.of(ScopeType.TEACHER)
    );

    private final String description;
    private final Set<ScopeType> permissions;

    /**
     * Constructor for the {@code CommandType} enum.
     *
     * @param description A brief description of the query type.
     * @param permissions The roles permitted to execute this query.
     */
    CommandType(String description, Set<ScopeType> permissions) {
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
