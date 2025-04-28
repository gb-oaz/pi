package com.pi.core_quiz.core.domain.itens;

import java.util.List;
import java.util.Map;

public interface IOperationsQuiz<T> {
    List<String> getAnswers();
    Map<String, List<T>> getAnswersLive();

    void addAnswerOperationLive(String login, String code, List<String> answerItem);

    default void addAnswerLive(String login, String code, List<T> value) {
        getAnswersLive().put(login + "#" + code, value);
    }
}
