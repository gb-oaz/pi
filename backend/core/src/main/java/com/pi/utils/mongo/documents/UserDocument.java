package com.pi.utils.mongo.documents;

import com.pi.core_user.core.domains.User;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.ObjectUtils;

import static com.pi.utils.mongo.constants.Collections.COLLECTION_USERS;

@Document(collection = COLLECTION_USERS)
public class UserDocument extends User {
    @Id String id;

    public UserDocument() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public static UserDocument mapperDocument(User user) {
        if (ObjectUtils.isEmpty(user)) return null;
        var doc = new UserDocument();
        doc.name(user.getName());
        doc.email(user.getEmail());
        doc.login(user.getLogin());
        doc.code(user.getCode());
        doc.password(user.getPassword());
        doc.scopes(user.getScopes());
        doc.createAt(user.getCreateAt());
        doc.updateAt(user.getUpdateAt());
        return doc;
    }

    public static User mapperUser(UserDocument userDocument) {
        if (ObjectUtils.isEmpty(userDocument)) return null;
        return User.builder()
                .name(userDocument.getName())
                .email(userDocument.getEmail())
                .login(userDocument.getLogin())
                .code(userDocument.getCode())
                .password(userDocument.getPassword())
                .scopes(userDocument.getScopes())
                .createAt(userDocument.getCreateAt())
                .updateAt(userDocument.getUpdateAt())
                .build();
    }
}