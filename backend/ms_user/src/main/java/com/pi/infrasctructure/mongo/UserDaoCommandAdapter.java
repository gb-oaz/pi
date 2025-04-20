package com.pi.infrasctructure.mongo;

import com.pi.core_auth.core.enums.ScopeType;
import com.pi.core_user.core.domains.User;
import com.pi.core_user.ports.out.IUserCommandOut;
import com.pi.utils.exceptions.GlobalException;

import com.pi.utils.mongo.documents.UserDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.time.OffsetDateTime;
import java.util.EnumSet;
import java.util.Objects;

import static com.pi.utils.mongo.constants.Collections.COLLECTION_USERS;

@Repository
public class UserDaoCommandAdapter implements IUserCommandOut {
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String LOGIN = "login";
    private static final String CODE = "code";
    private static final String PASSWORD = "password";
    private static final String UPDATE_AT = "updateAt";
    private static final String RESPONSE_NOT_FOUND = "User not found for update";

    private final MongoTemplate template;

    @Autowired
    public UserDaoCommandAdapter(MongoTemplate template) {
        this.template = template;
    }

    @Override
    public User createUser(String name, String email, String login, String code, String password, EnumSet<ScopeType> scopes) throws GlobalException {
        var user = User.builder()
                .name(name)
                .email(email)
                .login(login)
                .code(code)
                .password(password)
                .scopes(scopes)
                .createAt(OffsetDateTime.now().toString())
                .updateAt(OffsetDateTime.now().toString())
                .build();

        var document = template.insert(Objects.requireNonNull(UserDocument.mapperDocument(user)), COLLECTION_USERS);
        return UserDocument.mapperUser(document);
    }

    @Override
    public User updateUser(String login, String code, String name, String email, String password) throws GlobalException {
        template.updateFirst(buildQuery(login, code), buildUpdate(name, email, password), UserDocument.class);
        return validateAndReturnIfUserUpdated(login, code);
    }

    private Query buildQuery(String login, String code) {
        var query = new Query();
        query.addCriteria(Criteria.where(LOGIN).is(login));
        query.addCriteria(Criteria.where(CODE).is(code));
        return query;
    }

    private UpdateDefinition buildUpdate(String name, String email, String password) {
        Update update = new Update();

        if (!ObjectUtils.isEmpty(name)) update.set(NAME, name);
        if (!ObjectUtils.isEmpty(email)) update.set(EMAIL, email);
        if (!ObjectUtils.isEmpty(password)) update.set(PASSWORD, password);

        update.set(UPDATE_AT, OffsetDateTime.now().toString());

        return update;
    }

    private User validateAndReturnIfUserUpdated(String login, String code) {
        var document = template.findOne(buildQuery(login, code), UserDocument.class);
        if (ObjectUtils.isEmpty(document)) {
            throw GlobalException.builder()
                    .status(404)
                    .details(RESPONSE_NOT_FOUND)
                    .build();
        }
        return UserDocument.mapperUser(document);
    }
}
