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
        var dtoValid = dto.validate();
        existUserWithEmail(dtoValid);
        existUserWithLoginAndCode(dtoValid);
        var user = userCommandOut.createUser(dtoValid.getName(), dtoValid.getEmail(), dtoValid.getLogin(), dtoValid.getCode(), Crypt.encrypt(dtoValid.getPassword()), dtoValid.getScopes());
        LOG.info("End CaseCreateUser call.");
        user.setPassword("***********");
        return user;
    }

    /**
     * Checks if a user with the provided email already exists.
     *
     * @throws GlobalException If a user with the same email already exists.
     */
    protected void existUserWithEmail(User dtoValid) throws GlobalException {
        LOG.info("Init validation if exist user email.");
        var user = userQueryOut.getUserByProjection(dtoValid.getEmail(), null, null);
        if (!ObjectUtils.isEmpty(user)) {
            LOG.warn("{} - User email already exist", dtoValid.getEmail());
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
    protected void existUserWithLoginAndCode(User dtoValid) throws GlobalException {
        LOG.info("Init validation if exist user login and code.");
        var user = userQueryOut.getUserByProjection(null, dtoValid.getLogin(), dtoValid.getCode());
        if (!ObjectUtils.isEmpty(user)) {
            LOG.warn("{} # {} - User login and code already exist", dtoValid.getLogin(), dtoValid.getCode());
            throw GlobalException.builder()
                    .status(409)
                    .alert(new CustomAlert(SystemCodeEnum.C053PI))
                    .build();
        }
    }
}
