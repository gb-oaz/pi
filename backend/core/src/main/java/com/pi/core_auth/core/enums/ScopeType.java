package com.pi.core_auth.core.enums;

/**
 * Enum representing the scope types in the system.
 * Each scope type includes a description explaining its purpose.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * ScopeType scope = ScopeType.TEACHER;
 * System.out.println("Scope: " + scope.name());
 * System.out.println("Description: " + scope.getDescription());
 * }</pre>
 *
 * @author Gustavo
 * @since 1.0
 */
public enum ScopeType {
    /**
     * Represents a teacher scope, typically with permissions to manage courses and students.
     */
    TEACHER("A scope for teachers, allowing management of courses and students."),

    /**
     * Represents a student scope, typically with permissions to access course materials and submit assignments.
     */
    STUDENT("A scope for students, allowing access to course materials and submission of assignments."),

    /**
     * Represents an anonymous scope, typically with limited or no access to system resources.
     */
    ANONYMOUS("A scope for unauthenticated users with limited access to public resources.");

    private final String description;

    /**
     * Constructor for the {@code RoleType} enum.
     *
     * @param description A brief description of the scope type.
     */
    ScopeType(String description) {
        this.description = description;
    }

    /**
     * Returns the description of the scope type.
     *
     * @return A string representing the description of the scope type.
     */
    public String getDescription() {
        return description;
    }
}
