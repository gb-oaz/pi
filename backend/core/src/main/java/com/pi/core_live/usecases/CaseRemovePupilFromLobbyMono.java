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
public class CaseRemovePupilFromLobbyMono implements Callable<Mono<Live>> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseRemovePupilFromLobbyMono.class);

    private ILiveCommandOut liveCommandOut;

    private CommandDto dto;

    public CaseRemovePupilFromLobbyMono() { }

    public void setServices(ILiveCommandOut liveCommandOut) {
        this.liveCommandOut = liveCommandOut;
    }

    public void setDto(CommandDto dto) {
        this.dto = Objects.requireNonNull(dto, "Dto cannot be null");
    }

    @Override
    public Mono<Live> call() throws GlobalException {
        LOG.info("Init CaseRemovePupilFromLobbyMono call.");
        dto.validate();
        var response = liveCommandOut.removePupilFromLobby(dto.login(), dto.code(), dto.pupilLogin(), dto.pupilCode(), dto.keyLive());
        LOG.info("End CaseRemovePupilFromLobbyMono call.");
        return response;
    }
}
