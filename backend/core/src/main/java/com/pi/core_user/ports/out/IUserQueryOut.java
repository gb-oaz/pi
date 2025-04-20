package com.pi.core_user.ports.out;

import com.pi.core_user.core.domains.User;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.Pageable;

public interface IUserQueryOut {

    /**
     * Retrieve the user by projection.
     * <p>
     *     This method is used to retrieve the user by projection.
     * </p>
     * @param email the user's email, must be a valid email address
     * @param login the user's login, must be at least 8 characters with uppercase letters
     * @param code the user's code, must be at least 6 numbers
     * @return on {@link User} object
     * @throws GlobalException if occurs error during the process
     */
    User getUserByProjection(final String email, final String login, final String code) throws GlobalException;

    /**
     * Retrieve the users by projection.
     * <p>
     *     This method is used to retrieve the users by projection.
     * </p>
     * @param name the user's name, must be between 3 and 40 characters with space
     * @param email the user's email, must be a valid email address
     * @param login the user's login, must be at least 8 characters with uppercase letters
     * @param code the user's code, must be at least 6 numbers
     * @param page the page number, must be greater than or equal to 0
     * @param size the page size, must be greater than 0
     * @return on {@link Pageable} object containing the {@link User} users
     * @throws GlobalException if occurs error during the process
     */
    Pageable<User> getUsersByProjection(final String name, final String email, final String login, final String code, final Integer page, final Integer size) throws GlobalException;
}
