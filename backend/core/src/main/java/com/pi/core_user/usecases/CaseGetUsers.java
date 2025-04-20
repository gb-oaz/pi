package com.pi.core_user.usecases;

import com.pi.core_user.core.domains.User;
import com.pi.core_user.core.dtos.QueryDto;
import com.pi.core_user.ports.out.IUserQueryOut;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.Pageable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Service class for retrieving a page of users based on the provided query data transfer object.
 * <p>
 *     This class implements the {@link Callable} interface to retrieve a page of users.
 * </p>
 */
@Service
public class CaseGetUsers implements Callable<Pageable<User>> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseGetUsers.class);

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
    public CaseGetUsers() { }

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
     * Retrieves a page of users based on the provided query data transfer object.
     * <p>
     *     This method is used to retrieve a page of users based on the provided query
     *     data transfer object.
     * </p>
     * @return a {@link Pageable} object containing the users
     * @throws GlobalException if an error occurs during the process
     */
    @Override
    public Pageable<User> call() throws GlobalException {
        LOG.info("Init CaseGetUsers call.");
        dto.validate();
        var pageable = userQueryOut.getUsersByProjection(dto.name(), dto.email(), dto.login(), dto.code(), dto.page(), dto.size());
        pageable.getContent().forEach(user -> user.setPassword("***********"));
        LOG.info("End CaseGetUsers call.");
        return pageable;
    }
}
