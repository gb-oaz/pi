package com.pi.core_live.usecases;

import com.pi.core_live.ports.out.ILiveQueryOut;
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

    private ILiveQueryOut liveQueryOut;

    private QueryDto dto;

    public CaseGetQuizMono() { }

    public void setServices(ILiveQueryOut liveQueryOut) {
        this.liveQueryOut = liveQueryOut;
    }

    public void setDto(QueryDto dto) {
        this.dto = Objects.requireNonNull(dto, "Dto cannot be null");
    }

    @Override
    public Mono<Quiz> call() throws GlobalException {
        LOG.info("Init CaseGetQuizMono call.");
        dto.validate();
        var response = liveQueryOut.fetchQuiz(dto.key());
        LOG.info("End CaseGetQuizMono call.");
        return response;
    }
}
