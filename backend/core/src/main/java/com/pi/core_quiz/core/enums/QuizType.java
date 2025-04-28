package com.pi.core_quiz.core.enums;

import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.domain.quiz.*;

public enum QuizType {
    QUIZ_MULTIPLE_CHOICE(QuizMultipleChoice.class),
    QUIZ_FILL_SPACE(QuizFillSpace.class),
    QUIZ_TRUE_FALSE(QuizTrueFalse.class),
    QUIZ_OPEN(QuizOpen.class),
    QUIZ_POLL(QuizPoll.class),
    QUIZ_WORD_CLOUD(QuizWordCloud.class);

    private final Class<? extends IQuizItem> clazz;

    QuizType(Class<? extends IQuizItem> clazz) {
        this.clazz = clazz;
    }

    public Class<? extends IQuizItem> getClazz() {
        return clazz;
    }
}
