package com.pi.core_quiz.usecases;

import com.pi.core_quiz.core.domain.Quiz;
import com.pi.core_quiz.core.dtos.QueryDto;
import com.pi.core_quiz.ports.out.IQuizQueryOut;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Use case for retrieving quizzes based on specific criteria.
 * This class implements the Callable interface to allow for asynchronous execution.
 */
@Service
public class CaseGetQuizes implements Callable<Pageable<Quiz>> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseGetQuizes.class);

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
    public CaseGetQuizes() { }

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
     * Executes the use case to retrieve quizzes based on the provided criteria.
     *
     * @return A pageable list of Quiz objects matching the specified criteria.
     * @throws GlobalException if an error occurs during the retrieval process.
     */
    @Override
    public Pageable<Quiz> call() throws GlobalException {
        LOG.info("Init CaseGetQuiz call.");
        dto.validate();
        var response = quizQueryOut.findQuizByProjection(dto.login(), dto.code(), dto.name(), dto.categories(), dto.page(), dto.size());
        LOG.info("End CaseGetQuiz call.");
        return response;
    }
}
