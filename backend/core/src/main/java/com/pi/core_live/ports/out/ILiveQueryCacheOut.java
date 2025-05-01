package com.pi.core_live.ports.out;

import com.pi.core_live.core.domain.Live;

import com.pi.utils.exceptions.GlobalException;
import reactor.core.publisher.Mono;

/**
 * This interface is the output port cache for the live service, which is
 * responsible for providing the live class data to the client.
 */
public interface ILiveQueryCacheOut {
    /**
     * This method is used to fetch a live class by key.
     *
     * @param keyLive the key of the live class
     * @return a Mono that emits the live class or an empty Mono if the live class is not found
     * @Trhows GlobalException
     */
    Mono<Live> fetchLive(final String keyLive) throws GlobalException;
}
