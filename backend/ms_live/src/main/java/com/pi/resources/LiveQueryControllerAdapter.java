package com.pi.resources;

import com.pi.core_live.core.domain.Live;
import com.pi.core_live.core.dtos.QueryDto;
import com.pi.core_live.ports.in.ILiveQueryIn;
import com.pi.core_live.usecases.CaseGetLiveMono;
import com.pi.infrastructure.redis.config.LiveQueryCacheAdapter;
import com.pi.utils.exceptions.GlobalException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class LiveQueryControllerAdapter implements ILiveQueryIn {

    private final LiveQueryCacheAdapter liveQueryCacheAdapter;

    public LiveQueryControllerAdapter(LiveQueryCacheAdapter liveQueryCacheAdapter) { this.liveQueryCacheAdapter = liveQueryCacheAdapter; }

    @Override
    public Mono<ResponseEntity<Live>> getLive(String queryType, String keyLive, String authorization) throws GlobalException {
        var caseGetLiveMono = new CaseGetLiveMono();
        var dto = QueryDto.builder()
                .queryType(queryType)
                .keyLive(keyLive)
                .token(authorization)
                .build();

        caseGetLiveMono.setServices(liveQueryCacheAdapter);
        caseGetLiveMono.setDto(dto);
        return caseGetLiveMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Flux<Live> getLiveStream(String queryType, String keyLive, String authorization) throws GlobalException {
        return Flux.interval(Duration.ofSeconds(3))
                .flatMap(tick -> {
                    var caseGetLiveMono = new CaseGetLiveMono();
                    var dto = QueryDto.builder()
                            .queryType(queryType)
                            .keyLive(keyLive)
                            .token(authorization)
                            .build();

                    caseGetLiveMono.setServices(liveQueryCacheAdapter);
                    caseGetLiveMono.setDto(dto);
                    return caseGetLiveMono.call();
                })
                .distinctUntilChanged(Live::getUpdateOn)
                .doOnCancel(() -> {});
    }
}
