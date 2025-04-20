package com.pi.core_user.core.enums;

import com.pi.core_auth.core.enums.ScopeType;

import java.util.EnumSet;

public enum QueryType {

    QUERY_GET_USER_BY_PROJECTION(
        "Get user by projection. Use this query to retrieve user information based on specific criteria.",
        EnumSet.of(ScopeType.ANONYMOUS, ScopeType.TEACHER, ScopeType.STUDENT)
    ),

    QUERY_GET_USERS_BY_PROJECTION(
        "Get users by projection. Use this query to retrieve a list of users based on specific criteria.",
        EnumSet.of(ScopeType.TEACHER, ScopeType.STUDENT)
    );

    private final String description;
    private final EnumSet<ScopeType> permissions;

    QueryType(String description, EnumSet<ScopeType> permissions) {
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
