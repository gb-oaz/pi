package com.pi.infrasctructure.mongo;

import com.pi.core_quiz.core.domain.Quiz;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.utils.models.Response;
import com.pi.core_quiz.ports.out.IQuizCommandOut;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.mongo.documents.QuizDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.Set;

@Repository
public class QuizDaoCommandAdapter implements IQuizCommandOut {
    private static final String RESPONSE_NOT_FOUND = "Quiz not found";
    private static final String POSITION_ALREADY_EXISTS = "Position already exists";
    private static final String POSITION_NOT_EXISTS = "Position not exists";
    private static final String QUIZES = "quizes";
    private static final String KEY = "key";
    private static final String NAME = "name";
    private static final String CATEGORIES = "categories";

    @Value("${spring.data.mongodb.collections.quizes}") String COLLECTION_NAME;

    private final MongoTemplate template;

    @Autowired
    public QuizDaoCommandAdapter(MongoTemplate template) { this.template = template; }

    @Override
    public Response createNewQuiz(Quiz quiz) throws GlobalException {
        var document = template.insert(Objects.requireNonNull(QuizDocument.mapperDocument(quiz)), COLLECTION_NAME);
        return Response.builder().key(document.getKey()).build();
    }

    @Override
    public Response addQuizItem(String key, Integer position, IQuizItem item) throws GlobalException {
        var quiz = template.findOne(new Query(Criteria.where(KEY).is(key)), QuizDocument.class, COLLECTION_NAME);
        if (ObjectUtils.isEmpty(quiz)) throw GlobalException.builder().status(404).details(RESPONSE_NOT_FOUND).build();
        if (quiz.exist(position)) throw GlobalException.builder().status(400).details(POSITION_ALREADY_EXISTS).build();
        quiz.addQuizItem(position, item);
        template.updateFirst(new Query(Criteria.where(KEY).is(key)), new Update().set(QUIZES, quiz.getQuizes()), QuizDocument.class, COLLECTION_NAME);
        return Response.builder().key(quiz.getKey()).build();
    }

    @Override
    public Response updateQuizItem(String key, Integer position, IQuizItem item) throws GlobalException {
        var quiz = template.findOne(new Query(Criteria.where(KEY).is(key)), QuizDocument.class, COLLECTION_NAME);
        if (ObjectUtils.isEmpty(quiz)) throw GlobalException.builder().status(404).details(RESPONSE_NOT_FOUND).build();
        if (!quiz.exist(position)) throw GlobalException.builder().status(400).details(POSITION_NOT_EXISTS).build();
        quiz.updateQuizItem(position, item);
        template.updateFirst(new Query(Criteria.where(KEY).is(key)), new Update().set(QUIZES, quiz.getQuizes()), QuizDocument.class, COLLECTION_NAME);
        return Response.builder().key(quiz.getKey()).build();
    }

    @Override
    public Response updateQuiz(String key, String name, Set<String> categories) throws GlobalException {
        var quiz = template.findOne(new Query(Criteria.where(KEY).is(key)), QuizDocument.class, COLLECTION_NAME);
        if (ObjectUtils.isEmpty(quiz)) throw GlobalException.builder().status(404).details(RESPONSE_NOT_FOUND).build();

        var update = new Update();
        if (!ObjectUtils.isEmpty(name)) update.set(NAME, name);
        if (!ObjectUtils.isEmpty(categories)) update.set(CATEGORIES, categories);

        template.updateFirst(new Query(Criteria.where(KEY).is(key)), update, QuizDocument.class, COLLECTION_NAME);
        return Response.builder().key(quiz.getKey()).build();
    }

    @Override
    public Response deleteQuizItem(String key, Integer position) throws GlobalException {
        var quiz = template.findOne(new Query(Criteria.where(KEY).is(key)), QuizDocument.class, COLLECTION_NAME);
        if (ObjectUtils.isEmpty(quiz)) throw GlobalException.builder().status(404).details(RESPONSE_NOT_FOUND).build();
        quiz.deleteQuizItem(position);
        template.updateFirst(new Query(Criteria.where(KEY).is(key)), new Update().set(QUIZES, quiz.getQuizes()), QuizDocument.class, COLLECTION_NAME);
        return Response.builder().key(quiz.getKey()).build();
    }

    @Override
    public Response deleteQuiz(String key) throws GlobalException {
        var quiz = template.findOne(new Query(Criteria.where(KEY).is(key)), QuizDocument.class, COLLECTION_NAME);
        if (ObjectUtils.isEmpty(quiz)) throw GlobalException.builder().status(404).details(RESPONSE_NOT_FOUND).build();
        template.remove(new Query(Criteria.where(KEY).is(key)), QuizDocument.class, COLLECTION_NAME);
        return Response.builder().key(quiz.getKey()).build();
    }
}
