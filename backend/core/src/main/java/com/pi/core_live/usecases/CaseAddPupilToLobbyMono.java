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
public class CaseAddPupilToLobbyMono implements Callable<Mono<Live>>, ITokenCheck {
    private static final Logger LOG = LoggerFactory.getLogger(CaseAddPupilToLobbyMono.class);

    private ILiveCommandCacheOut liveCommandOut;
    private JwtDecoder jwtDecoder;
    private CommandDto dto;

    public CaseAddPupilToLobbyMono() { }

    /**
     * Sets the ILiveCommandCacheOut that will be used to add a pupil to the lobby of a live class.
     *
     * <p>This method requires a non-null {@link ILiveCommandCacheOut}. If the provided
     * {@link ILiveCommandCacheOut} is null, a {@link NullPointerException} will be thrown.
     *
     * @param liveCommandOut the ILiveCommandCacheOut to use for adding a pupil to the lobby of a live
     *     class.
     * @throws NullPointerException if {@code liveCommandOut} is null.
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
     * Sets the CommandDto that will be used to add a pupil to the lobby of a live class.
     *
     * <p>This method requires a non-null {@link CommandDto}. If the provided {@link CommandDto} is
     * null, a {@link NullPointerException} will be thrown.
     *
     * @param dto the CommandDto to use for adding a pupil to the lobby of a live class.
     * @throws NullPointerException if {@code dto} is null.
     */
    public void setDto(CommandDto dto) {
        this.dto = Objects.requireNonNull(dto, "Dto cannot be null");
    }

    /**
     * Adds a pupil to the lobby of the live class.
     *
     * <p>This method first validates the {@link CommandDto} and checks the pupil's credentials.
     *
     * @return a {@link Mono} that emits the updated live class when the operation is
     *     successfully executed.
     *
     * @throws GlobalException if the operation fails.
     */
    @Override
    public Mono<Live> call() throws GlobalException {
        LOG.info("Init CaseAddPupilToLobbyMono call.");
        dto.validate();
        checkCredentials(jwtDecoder, dto.token(), dto.pupilLogin(), dto.pupilCode());
        var response = liveCommandOut.addPupilToLobby(dto.pupilLogin(), dto.pupilCode(), dto.keyLive());
        LOG.info("End CaseAddPupilToLobbyMono call.");
        return response;
    }
}
