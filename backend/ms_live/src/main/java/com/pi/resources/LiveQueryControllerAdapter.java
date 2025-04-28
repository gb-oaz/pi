package com.pi.resources;

import com.pi.core_live.core.domain.Live;
import com.pi.core_live.core.dtos.QueryDto;
import com.pi.core_live.ports.in.ILiveQueryIn;
import com.pi.core_live.usecases.CaseGetLiveMono;
import com.pi.utils.exceptions.GlobalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class LiveQueryControllerAdapter implements ILiveQueryIn {

    private final CaseGetLiveMono caseGetLiveMono;

    public LiveQueryControllerAdapter(CaseGetLiveMono caseGetLiveMono) {
        this.caseGetLiveMono = caseGetLiveMono;
        this.caseGetLiveMono.setServices(null);
    }

    @Override
    public Mono<ResponseEntity<Live>> getLive(String queryType, String keyLive, String authorization) throws GlobalException {
        var dto = QueryDto.builder()
                .queryType(queryType)
                .keyLive(keyLive)
                .token(authorization)
                .build();

        caseGetLiveMono.setDto(dto);
        return caseGetLiveMono.call().map(ResponseEntity::ok);
    }
}
