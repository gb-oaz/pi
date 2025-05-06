package com.pi.infrastructure.redis.config;

import com.pi.core_live.core.domain.Live;
import com.pi.core_live.core.dtos.CommandDto;
import com.pi.core_live.core.enums.CommandType;
import com.pi.core_live.ports.out.ILiveCommandCacheOut;
import com.pi.core_live.usecases.CaseGetQuizMono;
import com.pi.core_live.usecases.CasePersistLiveMono;
import com.pi.core_quiz.core.dtos.QueryDto;
import com.pi.core_quiz.core.enums.QueryType;
import com.pi.infrastructure.mongo.LiveCommandPersistAdapter;
import com.pi.infrastructure.mongo.LiveQueryPersistAdapter;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class LiveCommandCacheAdapter implements ILiveCommandCacheOut {

    private final ReactiveRedisTemplate<String, Live> redisTemplate;
    private final LiveQueryPersistAdapter liveQueryPersistOut;
    private final LiveCommandPersistAdapter liveCommandPersistAdapter;
    private final JwtDecoder jwtDecoder;

    public LiveCommandCacheAdapter(
            ReactiveRedisTemplate<String, Live> redisTemplate,
            LiveQueryPersistAdapter liveQueryPersistOut,
            LiveCommandPersistAdapter liveCommandPersistAdapter,
            JwtDecoder jwtDecoder
    ) {
        this.redisTemplate = redisTemplate;
        this.liveQueryPersistOut = liveQueryPersistOut;
        this.liveCommandPersistAdapter = liveCommandPersistAdapter;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public Mono<Live> createLive(String teacherToken,String loginTeacher, String codeTeacher, String keyQuiz) throws GlobalException {
        var redisKey = "LIVE" + loginTeacher + "CODE" + codeTeacher;
        var caseGetQuizMono = new CaseGetQuizMono();
        var dto = QueryDto.builder().queryType(QueryType.QUERY_GET_QUIZ.name()).token(teacherToken).key(keyQuiz).build();
        caseGetQuizMono.setServices(liveQueryPersistOut);
        caseGetQuizMono.setDto(dto);

        @SuppressWarnings("BlockingMethodInNonBlockingContext") var live = new Live(loginTeacher, codeTeacher, caseGetQuizMono.call().block());
        return redisTemplate.opsForValue().set(redisKey, live).flatMap(success -> success
                ? Mono.just(live)
                : Mono.error(GlobalException.builder().status(500).alert(new CustomAlert(SystemCodeEnum.C001PI)).details("Not save live redis").build())
        );
    }

    @Override
    public Mono<Live> nextPosition(String keyLive) throws GlobalException {
        return redisTemplate.keys(keyLive).next().flatMap(key -> redisTemplate.opsForValue().get(key).flatMap(live -> {
            live.nextPosition();
            return redisTemplate.opsForValue().set(key, live).flatMap(success -> success
                    ? Mono.just(live)
                    : Mono.error(GlobalException.builder().status(500).alert(new CustomAlert(SystemCodeEnum.C001PI)).details("Don't next position live redis").build())
            );
        })).switchIfEmpty(Mono.error(GlobalException.builder().status(404).alert(new CustomAlert(SystemCodeEnum.C003PI)).details("Not found live redis").build()));
    }

    @Override
    public Mono<Live> previousPosition(String keyLive) throws GlobalException {
        return redisTemplate.keys(keyLive).next().flatMap(key -> redisTemplate.opsForValue().get(key).flatMap(live -> {
            live.previousPosition();
            return redisTemplate.opsForValue().set(key, live).flatMap(success -> success
                    ? Mono.just(live)
                    : Mono.error(GlobalException.builder().status(500).alert(new CustomAlert(SystemCodeEnum.C001PI)).details("Don't previous position live redis").build())
            );
        })).switchIfEmpty(Mono.error(GlobalException.builder().status(404).alert(new CustomAlert(SystemCodeEnum.C003PI)).details("Not found live redis").build()));
    }

    @Override
    public Mono<Live> addPupilToLobby(String loginPupil, String codePupil, String keyLive) throws GlobalException {
        return redisTemplate.keys(keyLive).next().flatMap(key -> redisTemplate.opsForValue().get(key).flatMap(live -> {
            live.addPupilToLobby(loginPupil, codePupil);
            return redisTemplate.opsForValue().set(key, live).flatMap(success -> success
                    ? Mono.just(live)
                    : Mono.error(GlobalException.builder().status(500).alert(new CustomAlert(SystemCodeEnum.C001PI)).details("Not add pupil live redis").build())
            );
        })).switchIfEmpty(Mono.error(GlobalException.builder().status(404).alert(new CustomAlert(SystemCodeEnum.C003PI)).details("Not found live redis").build()));
    }

    @Override
    public Mono<Live> removePupilFromLobby(String loginPupil, String codePupil, String keyLive) throws GlobalException {
        return redisTemplate.keys(keyLive).next().flatMap(key -> redisTemplate.opsForValue().get(key).flatMap(live -> {
            live.removePupilFromLobby(loginPupil, codePupil);
            return redisTemplate.opsForValue().set(key, live).flatMap(success -> success
                    ? Mono.just(live)
                    : Mono.error(GlobalException.builder().status(500).alert(new CustomAlert(SystemCodeEnum.C001PI)).details("Not remove pupil live redis").build())
            );
        })).switchIfEmpty(Mono.error(GlobalException.builder().status(404).alert(new CustomAlert(SystemCodeEnum.C003PI)).details("Not found live redis").build()));
    }

    @Override
    public Mono<Live> addPupilAnswerToQuiz(String loginPupil, String codePupil, String keyLive, List<String> answerItem) throws GlobalException {
        return redisTemplate.keys(keyLive).next().flatMap(key -> redisTemplate.opsForValue().get(key).flatMap(live -> {
            live.addPupilAnswerToQuiz(loginPupil, codePupil, answerItem);
            return redisTemplate.opsForValue().set(key, live).flatMap(success -> success
                    ? Mono.just(live)
                    : Mono.error(GlobalException.builder().status(500).alert(new CustomAlert(SystemCodeEnum.C001PI)).details("Not add pupil answer to quiz live redis").build())
            );
        })).switchIfEmpty(Mono.error(GlobalException.builder().status(404).alert(new CustomAlert(SystemCodeEnum.C003PI)).details("Not found live redis").build()));
    }

    @Override
    public Mono<Live> endLive(String tokenTeacher, String loginTeacher, String codeTeacher, String keyLive) throws GlobalException {
        return redisTemplate.keys(keyLive).next().flatMap(key -> redisTemplate.opsForValue().get(key).flatMap(live -> {
            var casePersistLiveMono = new CasePersistLiveMono();
            var dto = CommandDto.builder()
                    .commandType(CommandType.COMMAND_PATCH_END_LIVE.name())
                    .token(tokenTeacher)
                    .login(loginTeacher)
                    .code(codeTeacher)
                    .live(live)
                    .build();

            casePersistLiveMono.setServices(liveCommandPersistAdapter);
            casePersistLiveMono.setDecoder(jwtDecoder);
            casePersistLiveMono.setDto(dto);
            return casePersistLiveMono.call().flatMap(persistedLive -> redisTemplate.delete(key).flatMap(deleted -> deleted > 0
                        ? Mono.just(live)
                        : Mono.error(GlobalException.builder().status(500).alert(new CustomAlert(SystemCodeEnum.C001PI)).details("Failed to delete live from redis").build())
                    )
                ).onErrorResume(e -> Mono.error(GlobalException.builder().status(500).alert(new CustomAlert(SystemCodeEnum.C001PI)).details("Failed to persist live: " + e.getMessage()).build()));
        })).switchIfEmpty(Mono.error(GlobalException.builder().status(404).alert(new CustomAlert(SystemCodeEnum.C003PI)).details("Live not found in redis").build()));
    }
}
