package com.pi.core_user.usecases;

import com.pi.core_user.core.domains.User;
import com.pi.core_user.core.dtos.QueryDto;
import com.pi.core_user.ports.out.IUserQueryOut;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Service class for retrieving a user by email or login and code.
 * <p>
 *     This class implements the {@link Callable} interface to retrieve a user.
 * </p>
 */
@Service
public class CaseGetUser implements Callable<User> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseGetUser.class);

    /**
     * Output port for querying user-related data.
     */
    private IUserQueryOut userQueryOut;

    /**
     * Query DTO containing user data.
     */
    private QueryDto dto;

    /**
     * Constructor for the CaseGetUser class.
     */
    public CaseGetUser() { }

    /**
     * Sets the output port for querying user-related data.
     *
     * @param userQueryOut the output port for querying user-related data.
     */
    public void setServices(IUserQueryOut userQueryOut) {
        this.userQueryOut = userQueryOut;
    }

    /**
     * Sets the query data transfer object.
     *
     * @param queryDto the query data transfer object.
     * @throws NullPointerException if queryDto is null.
     */
    public void setQueryDto(QueryDto queryDto) {
        this.dto = Objects.requireNonNull(queryDto, "QueryDto cannot be null");
    }

    /**
     * Retrieves a user by email or login and code.
     * <p>
     *     This method is used to retrieve a user.
     * </p>
     * @return the {@link User} object
     * @throws GlobalException if occurs error during the process
     */
    @Override
    public User call() throws GlobalException {
        LOG.info("Init CaseGetUser call.");
        dto.validate();
        ifUserGetByLoginAndCodeCheck(dto.login(), dto.code());
        var user = findUserAndCheckIfUserExist();
        LOG.info("End CaseGetUser call.");
        return user;
    }

    /**
     * Retrieves a user by projection.
     * <p>
     *     This method is used to retrieve a user by projection.
     * </p>
     * @return the {@link User} object
     * @throws GlobalException if occurs error during the process
     */
    protected User findUserAndCheckIfUserExist() throws GlobalException {
        LOG.info("Init CaseGetUser findUserAndCheckIfUserExist call.");
        var user = userQueryOut.getUserByProjection(dto.email(), dto.login(), dto.code());
        if (!ObjectUtils.isEmpty(user)) {
            LOG.info("{} - {}#{} - User found.", dto.email(), dto.login(), dto.code());
            return user;
        } else {
            LOG.warn("{} - {}#{} - User not found.", dto.email(), dto.login(), dto.code());
            throw GlobalException.builder()
                    .status(404)
                    .alert(new CustomAlert(SystemCodeEnum.C003PI))
                    .build();
        }
    }

    /**
     * Checks if login and code exists.
     * <p>
     *     This method is used to check if login and code exists.
     * </p>
     * @param login the user's login
     * @param code the user's code
     * @throws GlobalException if occurs error during the process
     */
    protected void ifUserGetByLoginAndCodeCheck(String login, String code) throws GlobalException {
        LOG.info("Init CaseGetUser ifUserGetByLoginAndCodeCheck call.");
        if (!ObjectUtils.isEmpty(login) && ObjectUtils.isEmpty(code)) {
            LOG.warn("{} - Invalid code.", SystemCodeEnum.C032PI.name());
            throw GlobalException.builder()
                    .status(404)
                    .alert(new CustomAlert(SystemCodeEnum.C032PI))
                    .build();
        } else if (!ObjectUtils.isEmpty(code) && ObjectUtils.isEmpty(login)) {
            LOG.warn("{} - Invalid login.", SystemCodeEnum.C031PI.name());
            throw GlobalException.builder()
                    .status(404)
                    .alert(new CustomAlert(SystemCodeEnum.C031PI))
                    .build();
        }
    }
}
