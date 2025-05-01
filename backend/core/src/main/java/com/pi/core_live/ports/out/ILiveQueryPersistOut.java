package com.pi.core_live.ports.out;

import com.pi.core_quiz.core.domain.Quiz;
import com.pi.utils.exceptions.GlobalException;

import reactor.core.publisher.Mono;

/**
 * This interface is the output port persist for the live service, which is
 * responsible for providing the live class data to the client.
 */
public interface ILiveQueryPersistOut {
    /**
     * This method is used to fetch a quiz by key.
     *
     * @param keyQuiz the key of the quiz
     * @return a Mono that emits the quiz or an empty Mono if the quiz is not found
     * @Trhows GlobalException
     */
    Mono<Quiz> fetchQuiz(final String keyQuiz) throws GlobalException;
}
