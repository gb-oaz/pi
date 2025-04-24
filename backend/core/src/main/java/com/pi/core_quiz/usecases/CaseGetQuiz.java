package com.pi.core_quiz.usecases;

import com.pi.core_quiz.core.domain.Quiz;
import com.pi.core_quiz.core.dtos.QueryDto;
import com.pi.core_quiz.ports.out.IQuizQueryOut;
import com.pi.utils.exceptions.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Use case for retrieving a quiz by its key.
 * This class implements the Callable interface to allow for asynchronous execution.
 */
@Service
public class CaseGetQuiz implements Callable<Quiz> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseGetQuiz.class);

    /**
     * The output port for querying quizzes.
     */
    private IQuizQueryOut quizQueryOut;

    /**
     * The data transfer object containing the query parameters.
     */
    private QueryDto dto;

    /**
     * Default constructor.
     * Initializes the use case with the necessary dependencies.
     */
    public CaseGetQuiz() { }

    /**
     * Constructor with parameters.
     * Initializes the use case with the provided output port and data transfer object.
     *
     * @param quizQueryOut The output port for querying quizzes.
     */
    public void setServices(IQuizQueryOut quizQueryOut) {
        this.quizQueryOut = quizQueryOut;
    }

    /**
     * Sets the data transfer object for the use case.
     *
     * @param queryDto The data transfer object containing the query parameters.
     */
    public void setDto(QueryDto queryDto) {
        this.dto = Objects.requireNonNull(queryDto, "QueryDto cannot be null");
    }

    /**
     * Executes the use case to retrieve a quiz by its key.
     *
     * @return The Quiz object corresponding to the provided key.
     * @throws GlobalException if an error occurs during the retrieval process.
     */
    @Override
    public Quiz call() throws GlobalException {
        LOG.info("Init CaseGetQuiz call.");
        dto.validate();
        var response =  quizQueryOut.findQuizByKey(dto.key());
        LOG.info("End CaseGetQuiz call.");
        return response;
    }
}
