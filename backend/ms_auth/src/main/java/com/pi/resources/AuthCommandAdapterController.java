package com.pi.resources;

import com.pi.core_auth.core.domains.Token;
import com.pi.core_auth.core.dtos.CommandDto;
import com.pi.core_auth.ports.in.IAuthCommandIn;
import com.pi.core_auth.usecases.CasePostAnonymousToken;
import com.pi.core_auth.usecases.CasePostSignInToken;
import com.pi.infrastructure.mongo.UserDaoQueryAdapter;
import com.pi.utils.exceptions.GlobalException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthCommandAdapterController implements IAuthCommandIn {

    private final CasePostSignInToken casePostSignInToken;
    private final CasePostAnonymousToken casePostAnonymousToken;

    public AuthCommandAdapterController(
            CasePostSignInToken casePostSignInToken,
            CasePostAnonymousToken casePostAnonymousToken,
            UserDaoQueryAdapter userDaoAdapter
    ) {
        this.casePostSignInToken = casePostSignInToken;
        this.casePostAnonymousToken = casePostAnonymousToken;
        this.casePostSignInToken.setServices(userDaoAdapter);
    }

    @Override
    public ResponseEntity<Token> postSignInToken(String commandType, String authorization, String login, String code, String password) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(commandType)
                .anonymousToken(authorization)
                .login(login)
                .code(code)
                .password(password)
                .build();

        casePostSignInToken.setCommandDto(dto);
        return ResponseEntity.status(201).body(casePostSignInToken.call());
    }

    @Override
    public ResponseEntity<Token> postAnonymousToken(String commandType) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(commandType)
                .build();

        casePostAnonymousToken.setCommandDto(dto);
        return ResponseEntity.status(201).body(casePostAnonymousToken.call());
    }
}
