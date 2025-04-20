package com.pi.core_user.core.enums;

import com.pi.core_auth.core.enums.ScopeType;

import java.util.EnumSet;

public enum CommandType {

    COMMAND_POST_CREATE_USER_TEACHER(
        "Create a new user teacher. Use this command to register a new user in the system.",
        EnumSet.of(ScopeType.ANONYMOUS)
    ),

    COMMAND_POST_CREATE_USER_STUDENT(
        "Create a new user student. Use this command to register a new user in the system.",
        EnumSet.of(ScopeType.ANONYMOUS)
    ),

    COMMAND_PUT_UPDATE_USER(
        "Update an existing user. Use this command to modify user details.",
        EnumSet.of(ScopeType.TEACHER, ScopeType.STUDENT)
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
