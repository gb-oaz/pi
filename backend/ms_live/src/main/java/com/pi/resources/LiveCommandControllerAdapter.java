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
import com.pi.utils.exceptions.GlobalException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class LiveCommandControllerAdapter implements ILiveCommandIn {

    private final CaseCreateLiveMono caseCreateLiveMono;
    private final CaseNextPositionLiveMono caseNextPositionLiveMono;
    private final CasePreviousPositionLiveMono casePreviousPositionLiveMono;
    private final CaseRemovePupilFromLobbyMono caseRemovePupilFromLobbyMono;
    private final CaseAddPupilToLobbyMono caseAddPupilToLobbyMono;
    private final CaseAddPupilAnswerToQuizMono caseAddPupilAnswerToQuizMono;
    private final CaseEndLiveMono caseEndLiveMono;

    public LiveCommandControllerAdapter(
            CaseCreateLiveMono caseCreateLiveMono,
            CaseNextPositionLiveMono caseNextPositionLiveMono,
            CasePreviousPositionLiveMono casePreviousPositionLiveMono,
            CaseRemovePupilFromLobbyMono caseRemovePupilFromLobbyMono,
            CaseAddPupilToLobbyMono caseAddPupilToLobbyMono,
            CaseAddPupilAnswerToQuizMono caseAddPupilAnswerToQuizMono,
            CaseEndLiveMono caseEndLiveMono) {
        this.caseCreateLiveMono = caseCreateLiveMono;
        this.caseNextPositionLiveMono = caseNextPositionLiveMono;
        this.casePreviousPositionLiveMono = casePreviousPositionLiveMono;
        this.caseRemovePupilFromLobbyMono = caseRemovePupilFromLobbyMono;
        this.caseAddPupilToLobbyMono = caseAddPupilToLobbyMono;
        this.caseAddPupilAnswerToQuizMono = caseAddPupilAnswerToQuizMono;
        this.caseEndLiveMono = caseEndLiveMono;
        this.caseCreateLiveMono.setServices(null);
        this.caseNextPositionLiveMono.setServices(null);
        this.casePreviousPositionLiveMono.setServices(null);
        this.caseRemovePupilFromLobbyMono.setServices(null);
        this.caseAddPupilToLobbyMono.setServices(null);
        this.caseAddPupilAnswerToQuizMono.setServices(null);
        this.caseEndLiveMono.setServices(null);
    }

    @Override
    public Mono<ResponseEntity<Live>> postNewLive(String commandType, String authorization, String login, String code, String keyQuiz) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .login(login)
                .code(code)
                .keyQuiz(keyQuiz)
                .build();

        caseCreateLiveMono.setDto(dto);
        return caseCreateLiveMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Live>> patchNextPosition(String commandType, String authorization, String login, String code, String keyLive) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .login(login)
                .code(code)
                .keyLive(keyLive)
                .build();

        caseNextPositionLiveMono.setDto(dto);
        return caseNextPositionLiveMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Live>> patchPreviousPosition(String commandType, String authorization, String login, String code, String keyLive) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .login(login)
                .code(code)
                .keyLive(keyLive)
                .build();

        casePreviousPositionLiveMono.setDto(dto);
        return casePreviousPositionLiveMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Live>> patchRemovePupilFromLobby(String commandType, String authorization, String login, String code, String keyLive, String pupilLogin, String pupilCode) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .login(login)
                .code(code)
                .keyLive(keyLive)
                .pupilLogin(pupilLogin)
                .pupilCode(pupilCode)
                .build();

        caseRemovePupilFromLobbyMono.setDto(dto);
        return caseRemovePupilFromLobbyMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Live>> patchAddPupilToLobby(String commandType, String authorization, String keyLive, String pupilLogin, String pupilCode) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .keyLive(keyLive)
                .pupilLogin(pupilLogin)
                .pupilCode(pupilCode)
                .build();

        caseAddPupilToLobbyMono.setDto(dto);
        return caseAddPupilToLobbyMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Live>> patchAddPupilAnswerToQuiz(String commandType, String authorization, List<String> answerItem, String keyLive, String pupilLogin, String pupilCode) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .answerItem(answerItem)
                .keyLive(keyLive)
                .pupilLogin(pupilLogin)
                .pupilCode(pupilCode)
                .build();

        caseAddPupilAnswerToQuizMono.setDto(dto);
        return caseAddPupilAnswerToQuizMono.call().map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Live>> patchEndLive(String commandType, String authorization, String keyLive, String login, String code) throws GlobalException {
        var dto = CommandDto.builder()
                .commandType(commandType)
                .token(authorization)
                .keyLive(keyLive)
                .login(login)
                .code(code)
                .build();

        caseEndLiveMono.setDto(dto);
        return caseEndLiveMono.call().map(ResponseEntity::ok);
    }
}
