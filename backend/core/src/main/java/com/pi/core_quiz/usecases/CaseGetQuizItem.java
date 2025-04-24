package com.pi.core_quiz.usecases;

import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.dtos.QueryDto;
import com.pi.core_quiz.ports.out.IQuizQueryOut;
import com.pi.utils.exceptions.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Use case for retrieving a quiz item by its key and position.
 * This class implements the Callable interface to allow for asynchronous execution.
 */
@Service
public class CaseGetQuizItem implements Callable<IQuizItem> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseGetQuizItem.class);

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
    public CaseGetQuizItem() { }

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
     * Executes the use case to retrieve a quiz item by key and position.
     *
     * @return The Quiz object corresponding to the provided key.
     * @throws GlobalException if an error occurs during the retrieval process.
     */
    @Override
    public IQuizItem call() throws GlobalException {
        LOG.info("Init CaseGetQuizItem call.");
        dto.validate();
        var quiz = quizQueryOut.findQuizItemByKeyAndPosition(dto.key(), dto.position());
        LOG.info("End CaseGetQuizItem call.");
        return quiz;
    }
}
