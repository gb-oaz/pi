package com.pi.core_user.usecases;

import com.pi.core_user.core.domains.User;
import com.pi.core_user.core.dtos.CommandDto;
import com.pi.core_user.ports.out.IUserCommandOut;
import com.pi.core_user.ports.out.IUserQueryOut;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import com.pi.utils.services.Crypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Service class for handling the creation of users.
 * <p>
 * This class implements the {@link Callable} interface to create a new user
 * based on the provided command DTO. It validates the user data and checks for
 * existing users with the same email, login, or code before creating a new user.
 * </p>
 */
@Service
public class CaseCreateUser implements Callable<User> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseCreateUser.class);

    /**
     * Output port for user-related commands.
     */
    private IUserCommandOut userCommandOut;

    /**
     * Output port for querying user-related data.
     */
    private IUserQueryOut userQueryOut;

    /**
     * Command DTO containing user data.
     */
    private CommandDto dto;

    /**
     * Default constructor.
     */
    public CaseCreateUser() { }

    /**
     * Constructor with dependencies.
     *
     * @param userCommandOut Output port for user-related commands.
     * @param userQueryOut   Output port for querying user-related data.
     */
    public void setServices(IUserCommandOut userCommandOut, IUserQueryOut userQueryOut) {
        this.userCommandOut = userCommandOut;
        this.userQueryOut = userQueryOut;
    }

    /**
     * Sets the command DTO.
     *
     * @param commandDto Command DTO containing user data.
     */
    public void setCommandDto(CommandDto commandDto) {
        this.dto = Objects.requireNonNull(commandDto, "CommandDto cannot be null");
    }

    /**
     * Creates a new user based on the provided command DTO.
     *
     * @return The created user.
     * @throws GlobalException If any validation or creation error occurs.
     */
    @Override
    public User call() throws GlobalException {
        LOG.info("Init CaseCreateUser call.");
        dto.validate();
        existUserWithEmail();
        existUserWithLoginAndCode();
        var user = userCommandOut.createUser(dto.name(), dto.email(), dto.login(), dto.code(), Crypt.encrypt(dto.password()));
        LOG.info("End CaseCreateUser call.");
        return user;
    }

    /**
     * Checks if a user with the provided email already exists.
     *
     * @throws GlobalException If a user with the same email already exists.
     */
    protected void existUserWithEmail() throws GlobalException {
        LOG.info("Init validation if exist user email.");
        var user = userQueryOut.getUserByProjection(dto.email(), null, null);
        if (!ObjectUtils.isEmpty(user)) {
            LOG.warn("{} - User email already exist", dto.email());
            throw GlobalException.builder()
                    .status(409)
                    .alert(new CustomAlert(SystemCodeEnum.C052PI))
                    .build();
        }
    }

    /**
     * Checks if a user with the provided login and code already exists.
     *
     * @throws GlobalException If a user with the same login and code already exists.
     */
    protected void existUserWithLoginAndCode() throws GlobalException {
        LOG.info("Init validation if exist user login and code.");
        var user = userQueryOut.getUserByProjection(null, dto.login(), dto.code());
        if (!ObjectUtils.isEmpty(user)) {
            LOG.warn("{} # {} - User login and code already exist", dto.login(), dto.code());
            throw GlobalException.builder()
                    .status(409)
                    .alert(new CustomAlert(SystemCodeEnum.C053PI))
                    .build();
        }
    }
}
