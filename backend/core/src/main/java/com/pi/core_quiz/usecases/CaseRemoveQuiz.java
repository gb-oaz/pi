package com.pi.core_quiz.usecases;

import com.pi.core_quiz.core.dtos.CommandDto;
import com.pi.core_quiz.core.utils.models.Response;
import com.pi.core_quiz.ports.out.IQuizCommandOut;
import com.pi.utils.exceptions.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Service class for handling the removal of quizzes.
 * <p>
 * This class implements the {@link Callable} interface to remove a quiz
 * based on the provided command DTO. It validates the quiz data before removing
 * the quiz.
 * </p>
 */
@Service
public class CaseRemoveQuiz implements Callable<Response> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseRemoveQuiz.class);

    /**
     * Output port for quiz-related commands.
     */
    private IQuizCommandOut quizCommandOut;

    /**
     * Command DTO containing quiz data.
     */
    private CommandDto dto;

    /**
     * Default constructor.
     */
    public CaseRemoveQuiz() { }

    /**
     * Constructor with dependencies.
     *
     * @param quizCommandOut Output port for quiz-related commands.
     */
    public void setServices(IQuizCommandOut quizCommandOut) {
        this.quizCommandOut = quizCommandOut;
    }

    /**
     * Sets the command DTO containing quiz data.
     *
     * @param commandDto The command DTO to be set.
     */
    public void setDto(CommandDto commandDto) {
        this.dto = Objects.requireNonNull(commandDto, "CommandDto cannot be null");
    }

    /**
     * Executes the remove quiz operation.
     *
     * @return Response indicating the result of the operation.
     * @throws GlobalException if an error occurs during the operation.
     */
    @Override
    public Response call() throws GlobalException {
        LOG.info("Init CaseRemoveQuiz call.");
        dto.validate();
        var response = quizCommandOut.deleteQuiz(dto.key());
        LOG.info("End CaseRemoveQuiz call.");
        return response;
    }
}
