package com.pi.infrasctructure.mongo;

import com.pi.core_quiz.core.domain.Quiz;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.ports.out.IQuizQueryOut;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.Pageable;
import com.pi.utils.mongo.documents.QuizDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;

@Repository
public class QuizDaoQueryAdapter implements IQuizQueryOut {
    private static final String RESPONSE_NOT_FOUND = "Quiz not found";
    private static final String KEY = "key";
    private static final String LOGIN = "login";
    private static final String CODE = "code";
    private static final String NAME = "name";
    private static final String CATEGORIES = "categories";

    @Value("${spring.data.mongodb.collections.quizes}") String COLLECTION_NAME;

    private final MongoTemplate template;

    @Autowired
    public QuizDaoQueryAdapter(MongoTemplate template) { this.template = template; }

    @Override
    public Quiz findQuizByKey(String key) throws GlobalException {
        var quiz = template.findOne(new Query(Criteria.where(KEY).is(key)), Quiz.class, COLLECTION_NAME);
        if (ObjectUtils.isEmpty(quiz)) throw GlobalException.builder().status(404).details(RESPONSE_NOT_FOUND).build();
        return quiz;
    }

    @Override
    public Pageable<Quiz> findQuizByProjection(String login, String code, String name, Set<String> categories, Integer page, Integer size) throws GlobalException {
        var currentPage = (page != null && page > 0) ? page : 1;
        var pageSize = (size != null && size > 0) ? size : 10;

        var query = buildQuery(login, code, name, categories);
        var total = getTotalCount(query);
        var quizzes = getPaginatedQuizzes(query, currentPage, pageSize);

        var pagination = buildPagination(currentPage, pageSize, total);
        return new Pageable<Quiz>().builder()
                .content(quizzes)
                .pagination(pagination)
                .build();
    }

    @Override
    public IQuizItem findQuizItemByKeyAndPosition(String key, Integer position) throws GlobalException {
        var quiz = template.findOne(new Query(Criteria.where(KEY).is(key)), QuizDocument.class, COLLECTION_NAME);
        if (ObjectUtils.isEmpty(quiz)) throw GlobalException.builder().status(404).details(RESPONSE_NOT_FOUND).build();

        return quiz.getQuizes().stream()
                .filter(item -> item.getPosition().equals(position))
                .findFirst()
                .orElseThrow(() -> GlobalException.builder().status(404).details(RESPONSE_NOT_FOUND).build());
    }

    protected Query buildQuery(String login, String code, String name, Set<String> categories) {
        Query query = new Query();
        if (!ObjectUtils.isEmpty(login)) query.addCriteria(Criteria.where(LOGIN).is(login));
        if (!ObjectUtils.isEmpty(code)) query.addCriteria(Criteria.where(CODE).is(code));
        if (!ObjectUtils.isEmpty(name)) query.addCriteria(Criteria.where(NAME).regex(".*" + name + ".*", "i"));
        if (!ObjectUtils.isEmpty(categories)) query.addCriteria(Criteria.where(CATEGORIES).in(categories));
        return query;
    }

    protected Long getTotalCount(Query query) {
        return template.count(query, QuizDocument.class);
    }

    protected List<Quiz> getPaginatedQuizzes(Query query, Integer currentPage, Integer pageSize) {
        var skip = (currentPage - 1) * pageSize;
        query.skip(skip).limit(pageSize);
        return template.find(query, QuizDocument.class)
                .stream()
                .map(QuizDocument::mapperQuiz)
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
