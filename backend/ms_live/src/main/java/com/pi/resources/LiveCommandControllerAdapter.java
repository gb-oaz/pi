package com.pi.resources;

import com.pi.core_live.core.domain.Live;
import com.pi.core_live.core.dtos.CommandDto;
import com.pi.core_live.ports.in.ILiveCommandIn;
import com.pi.core_live.usecases.CaseAddPupilAnswerToQuizMono;
import com.pi.core_live.usecases.CaseAddPupilToLobbyMono;
import com.pi.core_live.usecases.CaseCreateLiveMono;
import com.pi.core_live.usecases.CaseEndLiveMono;
import com.pi.core_live.usecases.CaseNextPositionLiveMono;
import com.pi.core_live.usecases.CasePreviousPositionLiveMono;
import com.pi.core_live.usecases.CaseRemovePupilFromLobbyMono;
import com.pi.infrastructure.redis.config.LiveCommandCacheAdapter;
import com.pi.utils.exceptions.GlobalException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class LiveCommandControllerAdapter implements ILiveCommandIn {

    private final LiveCommandCacheAdapter liveCommandCacheAdapter;
    private final JwtDecoder jwtDecoder;

    public LiveCommandControllerAdapter(
            LiveCommandCacheAdapter liveCommandCacheAdapter,
            JwtDecoder jwtDecoder
    ) {
        this.liveCommandCacheAdapter = liveCommandCacheAdapter;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Mono<ResponseEntity<Live>> postNewLive(String commandType, String authorization, String login, String code, String keyQuiz) throws GlobalException {
        var caseCreateLiveMono = new CaseCreateLiveMono();
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .login(login)
                .code(code)
                .keyQuiz(keyQuiz)
                .build();

        caseCreateLiveMono.setServices(liveCommandCacheAdapter);
        caseCreateLiveMono.setDecoder(jwtDecoder);
        caseCreateLiveMono.setDto(dto);
        return caseCreateLiveMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Live>> patchNextPosition(String commandType, String authorization, String login, String code, String keyLive) throws GlobalException {
        var caseNextPositionLiveMono = new CaseNextPositionLiveMono();
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .login(login)
                .code(code)
                .keyLive(keyLive)
                .build();

        caseNextPositionLiveMono.setServices(liveCommandCacheAdapter);
        caseNextPositionLiveMono.setDecoder(jwtDecoder);
        caseNextPositionLiveMono.setDto(dto);
        return caseNextPositionLiveMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Live>> patchPreviousPosition(String commandType, String authorization, String login, String code, String keyLive) throws GlobalException {
        var casePreviousPositionLiveMono = new CasePreviousPositionLiveMono();
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .login(login)
                .code(code)
                .keyLive(keyLive)
                .build();

        casePreviousPositionLiveMono.setServices(liveCommandCacheAdapter);
        casePreviousPositionLiveMono.setDecoder(jwtDecoder);
        casePreviousPositionLiveMono.setDto(dto);
        return casePreviousPositionLiveMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Live>> patchRemovePupilFromLobby(String commandType, String authorization, String login, String code, String keyLive, String pupilLogin, String pupilCode) throws GlobalException {
        var caseRemovePupilFromLobbyMono = new CaseRemovePupilFromLobbyMono();
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .login(login)
                .code(code)
                .keyLive(keyLive)
                .pupilLogin(pupilLogin)
                .pupilCode(pupilCode)
                .build();

        caseRemovePupilFromLobbyMono.setServices(liveCommandCacheAdapter);
        caseRemovePupilFromLobbyMono.setDecoder(jwtDecoder);
        caseRemovePupilFromLobbyMono.setDto(dto);
        return caseRemovePupilFromLobbyMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Live>> patchAddPupilToLobby(String commandType, String authorization, String keyLive, String pupilLogin, String pupilCode) throws GlobalException {
        var caseAddPupilToLobbyMono = new CaseAddPupilToLobbyMono();
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .keyLive(keyLive)
                .pupilLogin(pupilLogin)
                .pupilCode(pupilCode)
                .build();

        caseAddPupilToLobbyMono.setServices(liveCommandCacheAdapter);
        caseAddPupilToLobbyMono.setDecoder(jwtDecoder);
        caseAddPupilToLobbyMono.setDto(dto);
        return caseAddPupilToLobbyMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Live>> patchAddPupilAnswerToQuiz(String commandType, String authorization, List<String> answerItem, String keyLive, String pupilLogin, String pupilCode) throws GlobalException {
        var caseAddPupilAnswerToQuizMono = new CaseAddPupilAnswerToQuizMono();
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .answerItem(answerItem)
                .keyLive(keyLive)
                .pupilLogin(pupilLogin)
                .pupilCode(pupilCode)
                .build();

        caseAddPupilAnswerToQuizMono.setServices(liveCommandCacheAdapter);
        caseAddPupilAnswerToQuizMono.setDecoder(jwtDecoder);
        caseAddPupilAnswerToQuizMono.setDto(dto);
        return caseAddPupilAnswerToQuizMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Live>> patchEndLive(String commandType, String authorization, String keyLive, String login, String code) throws GlobalException {
        var caseEndLiveMono = new CaseEndLiveMono();
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .keyLive(keyLive)
                .login(login)
                .code(code)
                .build();

        caseEndLiveMono.setServices(liveCommandCacheAdapter);
        caseEndLiveMono.setDecoder(jwtDecoder);
        caseEndLiveMono.setDto(dto);
        return caseEndLiveMono.call().map(ResponseEntity::ok);
    }
}
