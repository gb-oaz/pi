package com.pi.infrastructure.redis.live;

import com.pi.core_live.core.domain.Live;
import com.pi.core_live.ports.out.ILiveQueryCacheOut;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

@Repository
public class LiveQueryCacheAdapter implements ILiveQueryCacheOut {
    public static final String LIVE_NOT_FOUND_IN_REDIS = "Live not found in Redis";
    public static final String INVALID_DATA_TYPE_IN_REDIS = "Invalid data type in Redis";

    private final ReactiveRedisTemplate<String, Live> redisTemplate;

    public LiveQueryCacheAdapter(ReactiveRedisTemplate<String, Live> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Live> fetchLive(String keyLive) throws GlobalException {
        return redisTemplate.opsForValue().get(keyLive)
                .switchIfEmpty(Mono.error(GlobalException.builder()
                        .status(404)
                        .alert(new CustomAlert(SystemCodeEnum.C003PI))
                        .details(LIVE_NOT_FOUND_IN_REDIS)
                        .build()))
                .flatMap(live -> {
                    if (!ObjectUtils.isEmpty(live)) {
                        return Mono.just(live);
                    } else {
                        return Mono.error(GlobalException.builder()
                                .status(500)
                                .alert(new CustomAlert(SystemCodeEnum.C001PI))
                                .details(INVALID_DATA_TYPE_IN_REDIS)
                                .build());
                    }
                });
    }
}
