package com.pi.core_quiz.ports.out;

import com.pi.core_quiz.core.domain.Quiz;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.utils.models.Response;
import com.pi.utils.exceptions.GlobalException;

import java.util.Set;

/**
 * Interface for Quiz Command Output operations.
 * This interface defines methods for creating, updating, and deleting quizzes and quiz items.
 */
public interface IQuizCommandOut {

    /**
     * Creates a new quiz.
     *
     * @param quiz The quiz to be created.
     * @return A Response object containing the result of the operation.
     * @throws GlobalException if an error occurs during the process.
     */
    Response createNewQuiz(Quiz quiz) throws GlobalException;

    /**
     * Adds a quiz item to an existing quiz.
     *
     * @param key The key of the quiz to which the item will be added.
     * @param position The position where the item will be added.
     * @param item The quiz item to be added.
     * @return A Response object containing the result of the operation.
     * @throws GlobalException if an error occurs during the process.
     */
    Response addQuizItem(final String key, final Integer position, final IQuizItem item) throws GlobalException;

    /**
     * Updates an existing quiz item.
     *
     * @param key The key of the quiz to which the item belongs.
     * @param position The position of the item to be updated.
     * @param item The updated quiz item.
     * @return A Response object containing the result of the operation.
     * @throws GlobalException if an error occurs during the process.
     */
    Response updateQuizItem(final String key, final Integer position, final IQuizItem item) throws GlobalException;

    /**
     * Updates an existing quiz.
     *
     * @param key The key of the quiz to be updated.
     * @param name The new name for the quiz.
     * @param categories The new categories for the quiz.
     * @return A Response object containing the result of the operation.
     * @throws GlobalException if an error occurs during the process.
     */
    Response updateQuiz(final String key, final String name, final Set<String> categories) throws GlobalException;

    /**
     * Deletes a quiz item from an existing quiz.
     *
     * @param key The key of the quiz from which the item will be deleted.
     * @param position The position of the item to be deleted.
     * @return A Response object containing the result of the operation.
     * @throws GlobalException if an error occurs during the process.
     */
    Response deleteQuizItem(final String key, final Integer position) throws GlobalException;

    /**
     * Deletes an existing quiz.
     *
     * @param key The key of the quiz to be deleted.
     * @return A Response object containing the result of the operation.
     * @throws GlobalException if an error occurs during the process.
     */
    Response deleteQuiz(final String key) throws GlobalException;
}
