package com.pi.core_quiz.usecases;

import com.pi.core_quiz.core.domain.Quiz;
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
 * Service class for handling the creation of quizzes.
 * <p>
 * This class implements the {@link Callable} interface to create a new quiz
 * based on the provided command DTO. It validates the quiz data before creating
 * a new quiz.
 * </p>
 */
@Service
public class CaseCreateQuiz implements Callable<Response> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseCreateQuiz.class);

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
    public CaseCreateQuiz() { }

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
     * Executes the quiz creation process.
     *
     * @return A {@link Response} object containing the result of the operation.
     * @throws GlobalException if an error occurs during the process.
     */
    @Override
    public Response call() throws GlobalException {
        LOG.info("Init CaseCreateQuiz call.");
        var dtoValid = dto.validate();
        var response =  quizCommandOut.createNewQuiz((Quiz) dtoValid);
        LOG.info("End CaseCreateQuiz call.");
        return response;
    }
}
