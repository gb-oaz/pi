package com.pi.core_live.usecases;

import com.pi.core_live.core.domain.Live;
import com.pi.core_live.core.dtos.CommandDto;
import com.pi.core_live.ports.out.ILiveCommandOut;
import com.pi.utils.exceptions.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.Callable;

@Service
public class CaseAddPupilAnswerToQuizMono implements Callable<Mono<Live>> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseAddPupilAnswerToQuizMono.class);

    private ILiveCommandOut liveCommandOut;

    private CommandDto dto;

    public CaseAddPupilAnswerToQuizMono() { }

    public void setServices(ILiveCommandOut liveCommandOut) {
        this.liveCommandOut = liveCommandOut;
    }

    public void setDto(CommandDto dto) {
        this.dto = Objects.requireNonNull(dto, "Dto cannot be null");
    }

    @Override
    public Mono<Live> call() throws GlobalException {
        LOG.info("Init CaseAddPupilAnswerToQuizMono call.");
        dto.validate();
        var response = liveCommandOut.addPupilAnswerToQuiz(dto.pupilLogin(), dto.pupilCode(), dto.keyLive(), dto.answerItem());
        LOG.info("End CaseAddPupilAnswerToQuizMono call.");
        return response;
    }
}
