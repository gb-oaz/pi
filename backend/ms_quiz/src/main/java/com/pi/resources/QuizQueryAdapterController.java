package com.pi.resources;

import com.pi.core_quiz.core.domain.Quiz;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.dtos.QueryDto;
import com.pi.core_quiz.ports.in.IQuizQueryIn;
import com.pi.core_quiz.usecases.CaseGetQuiz;
import com.pi.core_quiz.usecases.CaseGetQuizItem;
import com.pi.core_quiz.usecases.CaseGetQuizes;
import com.pi.infrasctructure.mongo.QuizDaoQueryAdapter;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class QuizQueryAdapterController implements IQuizQueryIn {

    private final CaseGetQuiz caseGetQuiz;
    private final CaseGetQuizes caseGetQuizes;
    private final CaseGetQuizItem caseGetQuizItem;

    public QuizQueryAdapterController(
            CaseGetQuiz caseGetQuiz,
            CaseGetQuizes caseGetQuizes,
            CaseGetQuizItem caseGetQuizItem,
            QuizDaoQueryAdapter quizDaoQueryAdapter
    ) {
        this.caseGetQuiz = caseGetQuiz;
        this.caseGetQuizes = caseGetQuizes;
        this.caseGetQuizItem = caseGetQuizItem;
        this.caseGetQuiz.setServices(quizDaoQueryAdapter);
        this.caseGetQuizes.setServices(quizDaoQueryAdapter);
        this.caseGetQuizItem.setServices(quizDaoQueryAdapter);
    }

    @Override
    public ResponseEntity<Quiz> getQuiz(String key, String queryType, String token) throws GlobalException {
        var dto = QueryDto.builder()
                .key(key)
                .queryType(queryType)
                .token(token)
                .build();

        caseGetQuiz.setDto(dto);
        return ResponseEntity.status(200).body(caseGetQuiz.call());
    }

    @Override
    public ResponseEntity<Pageable<Quiz>> getQuizesProjection(String queryType, String token, String login, String code, String name, Set<String> categories, Integer page, Integer size) throws GlobalException {
        var dto = QueryDto.builder()
                .queryType(queryType)
                .token(token)
                .name(name)
                .categories(categories)
                .page(page)
                .size(size)
                .build();

        caseGetQuizes.setDto(dto);
        return ResponseEntity.status(200).body(caseGetQuizes.call());
    }

    @Override
    public ResponseEntity<IQuizItem> getQuizItem(String key, Integer position, String queryType, String teacherToken) throws GlobalException {
        var dto = QueryDto.builder()
                .key(key)
                .position(position)
                .queryType(queryType)
                .token(teacherToken)
                .build();

        caseGetQuizItem.setDto(dto);
        return ResponseEntity.status(200).body(caseGetQuizItem.call());
    }
}
