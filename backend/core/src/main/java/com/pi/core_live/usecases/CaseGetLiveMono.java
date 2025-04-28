package com.pi.core_live.usecases;

import com.pi.core_live.core.domain.Live;
import com.pi.core_live.core.dtos.QueryDto;
import com.pi.core_live.ports.out.ILiveQueryOut;
import com.pi.utils.exceptions.GlobalException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.Callable;

@Service
public class CaseGetLiveMono implements Callable<Mono<Live>> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseGetLiveMono.class);

    private ILiveQueryOut liveQueryOut;

    private QueryDto dto;

    public CaseGetLiveMono() { }

    public void setServices(ILiveQueryOut liveQueryOut) {
        this.liveQueryOut = liveQueryOut;
    }

    public void setDto(QueryDto dto) {
        this.dto = Objects.requireNonNull(dto, "Dto cannot be null");
    }

    @Override
    public Mono<Live> call() throws GlobalException {
        LOG.info("Init CaseGetLiveMono call.");
        dto.validate();
        var response = liveQueryOut.fetchLive(dto.keyLive());
        LOG.info("End CaseGetLiveMono call.");
        return response;
    }
}
