package com.pi.utils.mongo.documents;

import com.pi.core_quiz.core.domain.Quiz;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.ObjectUtils;

import static com.pi.utils.mongo.constants.Collections.COLLECTION_QUIZ;

@Document(collection = COLLECTION_QUIZ)
public class QuizDocument extends Quiz {
    @Id
    String id;

    public QuizDocument() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public static QuizDocument mapperDocument(Quiz quiz) {
        if (ObjectUtils.isEmpty(quiz)) return null;
        var doc = new QuizDocument();
        doc.key(quiz.getKey());
        doc.login(quiz.getLogin());
        doc.code(quiz.getCode());
        doc.name(quiz.getName());
        doc.quizes(quiz.getQuizes());
        doc.categories(quiz.getCategories());
        return doc;
    }

    public static Quiz mapperQuiz(QuizDocument quizDocument) {
        if (ObjectUtils.isEmpty(quizDocument)) return null;
        return Quiz.builder()
                .key(quizDocument.getKey())
                .login(quizDocument.getLogin())
                .code(quizDocument.getCode())
                .name(quizDocument.getName())
                .quizes(quizDocument.getQuizes())
                .categories(quizDocument.getCategories())
                .build();

    }
}
