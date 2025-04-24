package com.pi.core_quiz.core.enums;

import com.pi.core_auth.core.enums.ScopeType;

import java.util.EnumSet;

public enum CommandType {
    COMMAND_POST_NEW_QUIZ(
        "Post a new quiz. Use this command to create a new quiz.",
        EnumSet.of(ScopeType.TEACHER)
    ),
    COMMAND_POST_QUIZ_ITEM(
        "Post a quiz item. Use this command to add an item to a quiz.",
        EnumSet.of(ScopeType.TEACHER)
    ),
    COMMAND_PATCH_QUIZ_ITEM(
        "Patch a quiz item. Use this command to update an existing quiz item.",
        EnumSet.of(ScopeType.TEACHER)
    ),
    COMMAND_PUT_QUIZ(
        "Put a quiz. Use this command to update an existing quiz.",
        EnumSet.of(ScopeType.TEACHER)
    ),
    COMMAND_DELETE_QUIZ_ITEM(
        "Delete a quiz item. Use this command to remove an item from a quiz.",
        EnumSet.of(ScopeType.TEACHER)
    ),
    COMMAND_DELETE_QUIZ(
        "Delete a quiz. Use this command to remove a quiz from the system.",
        EnumSet.of(ScopeType.TEACHER)
    );

    private final String description;
    private final EnumSet<ScopeType> permissions;

    CommandType(String description, EnumSet<ScopeType> permissions) {
        this.description = description;
        this.permissions = permissions;
    }

    public String getDescription() {
        return description;
    }

    public EnumSet<ScopeType> getPermissions() {
        return permissions;
    }
}
