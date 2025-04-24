package com.pi.core_quiz.core.enums;

import com.pi.core_auth.core.enums.ScopeType;

import java.util.EnumSet;

public enum QueryType {

    QUERY_GET_QUIZ(
        "Get a quiz. Use this query to retrieve a quiz by its key.",
        EnumSet.of(ScopeType.TEACHER, ScopeType.ANONYMOUS, ScopeType.STUDENT)
    ),
    QUERY_GET_QUIZES_PROJECTION(
        "Get a quiz projection. Use this query to retrieve a quiz projection by its key.",
        EnumSet.of(ScopeType.TEACHER, ScopeType.ANONYMOUS, ScopeType.STUDENT)
    ),
    QUERY_GET_QUIZ_ITEM(
        "Get a quiz item. Use this query to retrieve a quiz item by its key.",
        EnumSet.of(ScopeType.TEACHER)
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
