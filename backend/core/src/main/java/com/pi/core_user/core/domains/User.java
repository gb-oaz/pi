package com.pi.core_user.core.domains;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pi.core_auth.core.enums.ScopeType;

import java.util.EnumSet;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String name;
    private String email;
    private String login;
    private String code;
    private String password;
    private String createAt;
    private String updateAt;
    private EnumSet<ScopeType> scopes;

    public User() { }

    public static User builder() { return new User(); }

    public User name(String name) { this.name = name; return this; }
    public User email(String email) { this.email = email; return this; }
    public User login(String login) { this.login = login; return this; }
    public User code(String code) { this.code = code; return this; }
    public User password(String password) { this.password = password; return this; }
    public User createAt(String createAt) { this.createAt = createAt; return this; }
    public User updateAt(String updateAt) { this.updateAt = updateAt; return this; }
    public User scopes(EnumSet<ScopeType> scopes) { this.scopes = scopes; return this; }

    public User build() { return this; }

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getLogin() { return login; }
    public String getCode() { return code; }
    public String getPassword() { return password; }
    public String getCreateAt() { return createAt; }
    public String getUpdateAt() { return updateAt; }
    public EnumSet<ScopeType> getScopes() { return scopes; }
}
