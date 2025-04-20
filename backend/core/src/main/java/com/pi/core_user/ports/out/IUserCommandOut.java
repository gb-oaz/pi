package com.pi.core_user.ports.out;

import com.pi.core_auth.core.enums.ScopeType;
import com.pi.core_user.core.domains.User;
import com.pi.utils.exceptions.GlobalException;

import java.util.EnumSet;

/**
 * Interface for handling user-related commands in the output port.
 * <p>
 * This interface defines the contract for creating and updating user entities.
 * </p>
 */
public interface IUserCommandOut {

    /**
     * Creates a new user entity.
     * <p>
     * This method creates a new user entity with the provided name, email, login, code, and password.
     * </p>
     * @param name the user's name, must be between 3 and 40 characters with space
     * @param email the user's email, must be a valid email address
     * @param login the user's login, must be at least 8 characters with uppercase letters
     * @param code the user's code, must be at least 6 numbers
     * @param password the user's password, must be at least 15 characters long with specific requirements
     * @param scopes the user's scopes, must be a set of {@link ScopeType}
     * @return on {@link User} created
     * @throws GlobalException if occurs error during the process
     */
    User createUser(final String name, final String email, final String login, final String code, final String password, final EnumSet<ScopeType> scopes) throws GlobalException;

    /**
     * Updates an existing user entity.
     * <p>
     * This method updates an existing user entity with the provided name, email, login, code, and password.
     * </p>
     * @param login the user's login, unique identifier with code
     * @param code the user's code, unique identifier with login
     * @param name the user's name, must be between 3 and 40 characters with space
     * @param email the user's email, must be a valid email address
     * @param password the user's password, must be at least 15 characters long with specific requirements
     * @return on {@link User} updated
     * @throws GlobalException if occurs error during the process
     */
    User updateUser(final String login, final String code, final String name, final String email, final String password) throws GlobalException;
}
