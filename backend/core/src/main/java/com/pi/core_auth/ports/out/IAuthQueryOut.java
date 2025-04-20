package com.pi.core_auth.ports.out;

import com.pi.core_auth.core.enums.ScopeType;
import com.pi.utils.exceptions.GlobalException;

import java.util.EnumSet;

/**
 * Interface for handling authentication-related commands in the output port.
 * <p>
 * This interface defines the contract for verifying the existence of a user
 * based on their login credentials and retrieving their associated roles.
 * </p>
 */
public interface IAuthQueryOut {

    /**
     * Checks if a user exists based on the provided login credentials and retrieves their roles.
     * <p>
     * This method validates the user's login, code, and password, and returns
     * the set of roles associated with the user if they exist.
     * </p>
     *
     * @param login the user's login, must be a valid identifier
     * @param code the user's code, typically used for verification
     * @param password the user's password, must meet security requirements
     * @return an {@link EnumSet} of {@link ScopeType} representing the roles associated with the user
     * @throws GlobalException if occurs error during the process
     */
    EnumSet<ScopeType> existUser(final String login, final String code, final String password) throws GlobalException;
}