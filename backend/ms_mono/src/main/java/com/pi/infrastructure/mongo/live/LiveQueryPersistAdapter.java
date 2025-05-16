package com.pi.infrastructure.mongo.live;

import com.pi.core_live.ports.out.ILiveQueryPersistOut;
import com.pi.core_quiz.core.domain.Quiz;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import com.pi.utils.mongo.documents.QuizDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Repository
public class LiveQueryPersistAdapter implements ILiveQueryPersistOut {
    private static final String KEY_NOT_FOUND = "Provide existing key quiz";
    private static final String KEY = "key";

    private final MongoTemplate template;

    @Autowired
    public LiveQueryPersistAdapter(MongoTemplate template) { this.template = template; }

    @Override
    public Mono<Quiz> fetchQuiz(String keyQuiz) throws GlobalException {
        var quiz = template.findOne(new Query(Criteria.where(KEY).is(keyQuiz)), QuizDocument.class);
        if (ObjectUtils.isEmpty(quiz))
            throw GlobalException.builder()
                    .status(404)
                    .details(KEY_NOT_FOUND)
                    .alert(new CustomAlert(SystemCodeEnum.C003PI))
                    .build();
        return Mono.just(Objects.requireNonNull(QuizDocument.mapperQuiz(quiz)));
    }
}
