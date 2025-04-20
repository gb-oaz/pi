package com.pi.infrastructure.mongo;

import com.pi.core_auth.core.enums.ScopeType;
import com.pi.core_auth.ports.out.IAuthQueryOut;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import com.pi.utils.services.Crypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.EnumSet;

@Repository
public class UserDaoQueryAdapter implements IAuthQueryOut {
    private static final String LOGIN = "login";
    private static final String CODE = "code";
    private static final String PASSWORD = "password";
    private static final String RESPONSE_NOT_FOUND = "User not found";

    private final MongoTemplate template;

    @Autowired
    public UserDaoQueryAdapter(MongoTemplate template) {
        this.template = template;
    }

    @Override
    public EnumSet<ScopeType> existUser(String login, String code, String password) throws GlobalException {
        var user = executeQueryAndCheckResponse(login, code, password);
        return user.getScopes();
    }

    protected UserDocument executeQueryAndCheckResponse(String login, String code, String password) {
        var user = template.findOne(buildQuery(login, code, password), UserDocument.class);
        if (ObjectUtils.isEmpty(user))
            throw GlobalException.builder()
                    .status(404)
                    .alert(new CustomAlert(SystemCodeEnum.C003PI))
                    .details(RESPONSE_NOT_FOUND)
                    .build();
        return user;
    }

    protected Query buildQuery(String login, String code, String password) {
        var query = new Query();
        query.addCriteria(Criteria.where(LOGIN).is(login));
        query.addCriteria(Criteria.where(CODE).is(code));
        query.addCriteria(Criteria.where(PASSWORD).is(Crypt.encrypt(password)));
        return query;
    }
}
