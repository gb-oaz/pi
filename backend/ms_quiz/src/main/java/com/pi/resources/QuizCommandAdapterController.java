package com.pi.resources;

import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.dtos.CommandDto;
import com.pi.core_quiz.core.enums.CommandType;
import com.pi.core_quiz.core.utils.models.Response;
import com.pi.core_quiz.ports.in.IQuizCommandIn;
import com.pi.core_quiz.usecases.CaseAddQuizItem;
import com.pi.core_quiz.usecases.CaseCreateQuiz;
import com.pi.core_quiz.usecases.CaseRemoveQuiz;
import com.pi.core_quiz.usecases.CaseRemoveQuizItem;
import com.pi.core_quiz.usecases.CaseUpdateQuiz;
import com.pi.core_quiz.usecases.CaseUpdateQuizItem;
import com.pi.infrasctructure.mongo.QuizDaoCommandAdapter;
import com.pi.utils.exceptions.GlobalException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class QuizCommandAdapterController implements IQuizCommandIn {

    private final CaseCreateQuiz caseCreateQuiz;
    private final CaseAddQuizItem caseAddQuizItem;
    private final CaseUpdateQuizItem caseUpdateQuizItem;
    private final CaseUpdateQuiz caseUpdateQuiz;
    private final CaseRemoveQuizItem caseRemoveQuizItem;
    private final CaseRemoveQuiz caseRemoveQuiz;

    public QuizCommandAdapterController(
            CaseCreateQuiz caseCreateQuiz,
            CaseAddQuizItem caseAddQuizItem,
            CaseUpdateQuizItem caseUpdateQuizItem,
            CaseUpdateQuiz caseUpdateQuiz,
            CaseRemoveQuizItem caseRemoveQuizItem,
            CaseRemoveQuiz caseRemoveQuiz,
            QuizDaoCommandAdapter quizDaoCommandAdapter
    ) {
        this.caseCreateQuiz = caseCreateQuiz;
        this.caseAddQuizItem = caseAddQuizItem;
        this.caseUpdateQuizItem = caseUpdateQuizItem;
        this.caseUpdateQuiz = caseUpdateQuiz;
        this.caseRemoveQuizItem = caseRemoveQuizItem;
        this.caseRemoveQuiz = caseRemoveQuiz;
        this.caseCreateQuiz.setServices(quizDaoCommandAdapter);
        this.caseAddQuizItem.setServices(quizDaoCommandAdapter);
        this.caseUpdateQuizItem.setServices(quizDaoCommandAdapter);
        this.caseUpdateQuiz.setServices(quizDaoCommandAdapter);
        this.caseRemoveQuizItem.setServices(quizDaoCommandAdapter);
        this.caseRemoveQuiz.setServices(quizDaoCommandAdapter);
    }

    @Override
    public ResponseEntity<Response> postNewQuiz(String commandType, String teacherToken, String login, String code, String name, Set<String> categories) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(CommandType.valueOf(commandType))
                .token(teacherToken)
                .login(login)
                .code(code)
                .name(name)
                .categories(categories)
                .build();

        caseCreateQuiz.setDto(dto);
        return ResponseEntity.status(201).body(caseCreateQuiz.call());
    }

    @Override
    public ResponseEntity<Response> postQuizItem(String key, Integer position, String commandType, String typeItem, String teacherToken, IQuizItem quizItem) throws GlobalException {
        var dto = CommandDto.builder()
                .key(key)
                .position(position)
                .commandType(CommandType.valueOf(commandType))
                .typeItem(typeItem)
                .token(teacherToken)
                .quizItem(quizItem)
                .build();

        caseAddQuizItem.setDto(dto);
        return ResponseEntity.status(201).body(caseAddQuizItem.call());
    }

    @Override
    public ResponseEntity<Response> patchQuizItem(String key, Integer position, String commandType, String typeItem, String teacherToken, IQuizItem quizItem) throws GlobalException {
        var dto = CommandDto.builder()
                .key(key)
                .position(position)
                .commandType(CommandType.valueOf(commandType))
                .typeItem(typeItem)
                .token(teacherToken)
                .quizItem(quizItem)
                .build();

        caseUpdateQuizItem.setDto(dto);
        return ResponseEntity.status(200).body(caseUpdateQuizItem.call());
    }

    @Override
    public ResponseEntity<Response> putQuiz(String key, String commandType, String teacherToken, String name, Set<String> categories) throws GlobalException {
        var dto = CommandDto.builder()
                .key(key)
                .commandType(CommandType.valueOf(commandType))
                .token(teacherToken)
                .name(name)
                .categories(categories)
                .build();

        caseUpdateQuiz.setDto(dto);
        return ResponseEntity.status(200).body(caseUpdateQuiz.call());
    }

    @Override
    public ResponseEntity<Response> deleteQuizItem(String key, Integer position, String commandType, String teacherToken) throws GlobalException {
        var dto = CommandDto.builder()
                .key(key)
                .position(position)
                .commandType(CommandType.valueOf(commandType))
                .token(teacherToken)
                .build();

        caseRemoveQuizItem.setDto(dto);
        return ResponseEntity.status(200).body(caseRemoveQuizItem.call());
    }

    @Override
    public ResponseEntity<Response> deleteQuiz(String key, String commandType, String teacherToken) throws GlobalException {
        var dto = CommandDto.builder()
                .key(key)
                .commandType(CommandType.valueOf(commandType))
                .token(teacherToken)
                .build();

        caseRemoveQuiz.setDto(dto);
        return ResponseEntity.status(200).body(caseRemoveQuiz.call());
    }
}
