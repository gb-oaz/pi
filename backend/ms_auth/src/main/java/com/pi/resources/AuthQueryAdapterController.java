package com.pi.resources;

import com.pi.core_auth.core.dtos.QueryDto;
import com.pi.core_auth.core.enums.QueryType;
import com.pi.core_auth.core.utils.models.Response;
import com.pi.core_auth.ports.in.IAuthQueryIn;
import com.pi.core_auth.usecases.CaseGetScopeToken;
import com.pi.core_auth.usecases.CaseGetStatusToken;
import com.pi.utils.exceptions.GlobalException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthQueryAdapterController implements IAuthQueryIn {
    private final CaseGetStatusToken caseGetStatusToken;
    private final CaseGetScopeToken caseGetScopeToken;

    public AuthQueryAdapterController(
            CaseGetStatusToken caseGetStatusToken,
            CaseGetScopeToken caseGetScopeToken
    ) {
        this.caseGetStatusToken = caseGetStatusToken;
        this.caseGetScopeToken = caseGetScopeToken;
    }

    @Override
    public ResponseEntity<Response> getStatusToken(String queryType, String authorization) throws GlobalException {
        var dto = QueryDto.builder()
                .queryType(QueryType.valueOf(queryType))
                .token(authorization)
                .build();

        caseGetStatusToken.setQueryDto(dto);
        return ResponseEntity.status(200).body(caseGetStatusToken.call());
    }

    @Override
    public ResponseEntity<Response> getScopeToken(String queryType, String authorization) throws GlobalException {
        var dto = QueryDto.builder()
                .queryType(QueryType.valueOf(queryType))
                .token(authorization)
                .build();

        caseGetScopeToken.setQueryDto(dto);
        return ResponseEntity.status(200).body(caseGetScopeToken.call());
    }
}
