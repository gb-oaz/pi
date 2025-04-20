package com.pi.infrasctructure.mongo;

import com.pi.core_user.core.domains.User;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.pi.utils.constants.Collections.COLLECTION_USERS;

@Document(collection = COLLECTION_USERS)
public class UserDocument extends User {
    @Id String id;

    public UserDocument() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public User toUser() {
        return User.builder()
                .name(getName())
                .email(getEmail())
                .login(getLogin())
                .code(getCode())
                .password(getPassword())
                .scopes(getScopes())
                .createAt(getCreateAt())
                .updateAt(getUpdateAt())
                .build();
    }
}