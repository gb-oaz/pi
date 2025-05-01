package com.pi.core_live.usecases;

import com.pi.core_live.ports.out.ILiveQueryPersistOut;
import com.pi.core_quiz.core.domain.Quiz;
import com.pi.core_quiz.core.dtos.QueryDto;
import com.pi.utils.exceptions.GlobalException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.Callable;

@Service
public class CaseGetQuizMono implements Callable<Mono<Quiz>> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseGetQuizMono.class);

    private ILiveQueryPersistOut liveQueryPersistOut;
    private QueryDto dto;

    public CaseGetQuizMono() { }

    /**
     * Sets the output port for the live service, which is responsible for providing
     * the live class data to the client.
     *
     * @param liveQueryOut the output port for the live service
     */
    public void setServices(ILiveQueryPersistOut liveQueryOut) {
        this.liveQueryPersistOut = liveQueryOut;
    }

    /**
     * Sets the query data transfer object.
     *
     * @param dto the {@link QueryDto} containing the query details.
     */
    public void setDto(QueryDto dto) {
        this.dto = Objects.requireNonNull(dto, "Dto cannot be null");
    }

    /**
     * Retrieves a quiz by its key.
     *
     * @return a Mono that emits the quiz or an empty Mono if the quiz is not found
     * @throws GlobalException if an error occurs during the query
     */
    @Override
    public Mono<Quiz> call() throws GlobalException {
        LOG.info("Init CaseGetQuizMono call.");
        dto.validate();
        var response = liveQueryPersistOut.fetchQuiz(dto.key());
        LOG.info("End CaseGetQuizMono call.");
        return response;
    }
}
