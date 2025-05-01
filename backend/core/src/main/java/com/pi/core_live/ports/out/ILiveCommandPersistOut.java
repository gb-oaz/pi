package com.pi.core_live.ports.out;

import com.pi.core_live.core.domain.Live;
import com.pi.utils.exceptions.GlobalException;
import reactor.core.publisher.Mono;

/**
 * This interface represents the output persist port for the live service, which is
 * responsible for performing the commands related to the live class.
 *
 * @author  Gustavo Boaz
 * @version 1.0.0
 * @since   1.0.0
 */
public interface ILiveCommandPersistOut {
    /**
     * Persists the live class.
     *
     * @param live The live class.
     * @return A Mono that emits the persisted live class.
     * @throws GlobalException if an error occurs during the process.
     */
    Mono<Live> persistLive(final Live live) throws GlobalException;
}
