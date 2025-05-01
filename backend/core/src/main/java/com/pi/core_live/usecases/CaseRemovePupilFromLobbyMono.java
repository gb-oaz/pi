package com.pi.core_live.usecases;

import com.pi.core_auth.core.utils.interfaces.ITokenCheck;
import com.pi.core_live.core.domain.Live;
import com.pi.core_live.core.dtos.CommandDto;
import com.pi.core_live.ports.out.ILiveCommandCacheOut;
import com.pi.utils.exceptions.GlobalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.Callable;

@Service
public class CaseRemovePupilFromLobbyMono implements Callable<Mono<Live>>, ITokenCheck {
    private static final Logger LOG = LoggerFactory.getLogger(CaseRemovePupilFromLobbyMono.class);

    private ILiveCommandCacheOut liveCommandOut;
    private JwtDecoder jwtDecoder;
    private CommandDto dto;

    public CaseRemovePupilFromLobbyMono() { }

    /**
     * Set the ILiveCommandCacheOut that will be used to remove a pupil from a lobby.
     *
     * @param liveCommandOut ILiveCommandCacheOut that will be used to remove a pupil from a lobby.
     */
    public void setServices(ILiveCommandCacheOut liveCommandOut) {
        this.liveCommandOut = liveCommandOut;
    }

    /**
     * Sets the JwtDecoder to use for validating the JWT token.
     * @param jwtDecoder the JwtDecoder to use for validating the JWT token.
     */
    public void setDecoder(JwtDecoder jwtDecoder) { this.jwtDecoder = jwtDecoder; }

    /**
     * Sets the CommandDto that will be used to remove a pupil from a lobby.
     *
     * @param dto the CommandDto to use for removing a pupil from a lobby.
     *
     * @throws NullPointerException if {@code dto} is null.
     */
    public void setDto(CommandDto dto) {
        this.dto = Objects.requireNonNull(dto, "Dto cannot be null");
    }

    /**
     * Removes a pupil from the lobby of the live class.
     *
     * <p>This method validates the {@link CommandDto} and checks the teacher's credentials.
     *
     * @return a {@link Mono} that emits a {@link Live} object when the use case is successfully
     *     executed.
     *
     * @throws GlobalException if the use case fails.
     */
    @Override
    public Mono<Live> call() throws GlobalException {
        LOG.info("Init CaseRemovePupilFromLobbyMono call.");
        dto.validate();
        checkCredentials(jwtDecoder, dto.token(), dto.login(), dto.code());
        var response = liveCommandOut.removePupilFromLobby(dto.pupilLogin(), dto.pupilCode(), dto.keyLive());
        LOG.info("End CaseRemovePupilFromLobbyMono call.");
        return response;
    }
}
