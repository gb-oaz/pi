package com.pi.core_quiz.ports.out;

import com.pi.core_quiz.core.domain.Quiz;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.Pageable;

import java.util.Set;

/**
 * Interface for Quiz Query Output operations.
 * This interface defines methods for querying quizzes and quiz items.
 */
public interface IQuizQueryOut {

    /**
     * Retrieves a quiz by its key.
     *
     * @param key The key of the quiz to be retrieved.
     * @return The Quiz object corresponding to the provided key.
     * @throws GlobalException if an error occurs during the retrieval process.
     */
    Quiz findQuizByKey(final String key) throws GlobalException;;

    /**
     * Retrieves a quiz by its projection
     *
     * @param login The login of the user requesting the quiz.
     * @param code The code of the user requesting the quiz.
     * @param name The name of the quiz to be retrieved.
     * @param categories The categories of the quiz to be retrieved.
     * @param page The page number for pagination.
     * @param size The size of the page for pagination.
     * @return The Quiz object corresponding to the provided key and login.
     * @throws GlobalException if an error occurs during the retrieval process.
     */
    Pageable<Quiz> findQuizByProjection(final String login, final String code, final String name, final Set<String> categories, final Integer page, final Integer size) throws GlobalException;

    /**
     * Retrieves a quiz item by its key and position.
     *
     * @param key The key of the quiz to which the item belongs.
     * @param position The position of the item to be retrieved.
     * @return The Quiz item object corresponding to the provided key and position.
     * @throws GlobalException if an error occurs during the retrieval process.
     */
    IQuizItem findQuizItemByKeyAndPosition(final String key, final Integer position) throws GlobalException;
}
