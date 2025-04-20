package com.pi.infrasctructure.mongo;

import com.pi.core_user.core.domains.User;
import com.pi.core_user.ports.out.IUserQueryOut;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import com.pi.utils.models.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Repository
public class UserDaoQueryAdapter implements IUserQueryOut {
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String LOGIN = "login";
    private static final String CODE = "code";
    private static final String RESPONSE_NOT_FOUND = "User not found";

    private final MongoTemplate template;

    @Autowired
    public UserDaoQueryAdapter(MongoTemplate template) {
        this.template = template;
    }

    @Override
    public User getUserByProjection(String email, String login, String code) throws GlobalException {
        var document = executeQueryAndCheckResponse(email, login, code);
        return document.toUser();
    }

    @Override
    public Pageable<User> getUsersByProjection(String name, String email, String login, String code, Integer page, Integer size) throws GlobalException {
        var currentPage = (page != null && page > 0) ? page : 1;
        var pageSize = (size != null && size > 0) ? size : 10;

        var query = buildQuery(name, email, login, code);
        var total = getTotalCount(query);
        var users = getPaginatedUsers(query, currentPage, pageSize);

        var pagination = buildPagination(currentPage, pageSize, total);
        return new Pageable<User>()
                .builder()
                .content(users)
                .pagination(pagination)
                .build();
    }

    protected UserDocument executeQueryAndCheckResponse(String email, String login, String code) {
        return template.findOne(buildQuery(email, login, code), UserDocument.class);
    }

    protected Query buildQuery(String email, String login, String code) {
        var query = new Query();
        query.addCriteria(Criteria.where(EMAIL).is(email));
        query.addCriteria(Criteria.where(LOGIN).is(login));
        query.addCriteria(Criteria.where(CODE).is(code));
        return query;
    }

    protected Query buildQuery(String name, String email, String login, String code) {
        Query query = new Query();
        if (!ObjectUtils.isEmpty(name)) query.addCriteria(Criteria.where(NAME).regex(".*" + name + ".*", "i"));
        if (!ObjectUtils.isEmpty(email)) query.addCriteria(Criteria.where(EMAIL).is(email));
        if (!ObjectUtils.isEmpty(login)) query.addCriteria(Criteria.where(LOGIN).is(login));
        if (!ObjectUtils.isEmpty(code)) query.addCriteria(Criteria.where(CODE).is(code));
        return query;
    }

    protected Long getTotalCount(Query query) {
        return template.count(query, UserDocument.class);
    }

    protected List<User> getPaginatedUsers(Query query, Integer currentPage, Integer pageSize) {
        var skip = (currentPage - 1) * pageSize;
        query.skip(skip).limit(pageSize);
        return template.find(query, UserDocument.class)
                .stream()
                .map(UserDocument::toUser)
                .toList();
    }

    protected Pageable.Pagination buildPagination(int page, int size, long total) {
        return new Pageable.Pagination()
                .builder()
                .page(page)
                .size(size)
                .total((int) total)
                .build();
    }
}
