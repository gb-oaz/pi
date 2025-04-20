package com.pi.core_user.usecases;

import com.pi.core_auth.core.utils.constants.Claim;
import com.pi.core_auth.core.utils.services.Utils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Service class for handling user updates.
 * <p>
 *     This class implements the {@link Callable} interface to update a user
 *     based on the provided command DTO. It validates the user data and checks for
 *     existing users with the same email, login, or code before creating a new user.
 * </p>
 */
@Service
public class CaseUpdateUser implements Callable<User> {
    private static final Logger LOG = LoggerFactory.getLogger(CaseUpdateUser.class);

    /**
     * JWT decoder to decode the JWT token.
     */
    private final JwtDecoder jwtDecoder;

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
     * Constructs a {@code CaseUpdateUser} instance with a provided {@link JwtEncoder}.
     *
     * @param jwtDecoder the {@link JwtDecoder} used to decode JWT tokens.
     */
    @Autowired
    public CaseUpdateUser(JwtDecoder jwtDecoder) { this.jwtDecoder = jwtDecoder; }

    /**
     * Sets the output ports for user-related commands and data queries.
     *
     * @param userCommandOut the output port for user-related commands.
     * @param userQueryOut   the output port for querying user-related data.
     */
    public void setServices(IUserCommandOut userCommandOut, IUserQueryOut userQueryOut) {
        this.userCommandOut = userCommandOut;
        this.userQueryOut = userQueryOut;
    }

    /**
     * Sets the command data transfer object.
     *
     * @param commandDto the {@link CommandDto} containing the command details.
     */
    public void setCommandDto(CommandDto commandDto) {
        this.dto = Objects.requireNonNull(commandDto, "CommandDto cannot be null");
    }

    /**
     * Executes the user update process.
     * <p>
     *     This method validates the command DTO, finds the user to update if they exist,
     *     checks for email and password updates, and updates the user with the provided
     *     information.
     * </p>
     * @return the updated user entity.
     * @throws GlobalException if any validation or creation error occurs.
     */
    @Override
    public User call() throws GlobalException {
        LOG.info("Init CaseUpdateUser call.");
        dto.validate();
        var userExist = findUserIfExist(dto.token());
        ifUserUpdateEmailCheck(dto.email());
        ifUserUpdatePasswordCheck(userExist, dto.oldPassword());
        var user = userCommandOut.updateUser(userExist.getLogin(), userExist.getCode(), dto.name(), dto.email(), ifUserUpdatePasswordGet());
        LOG.info("End CaseUpdateUser call.");
        user.setPassword("***********");
        return user;
    }

    /**
     * Finds a user if they exist based on the provided token.
     *
     * @param token the JWT token containing the user's login and code.
     * @return the user entity if they exist.
     * @throws GlobalException if the user does not exist.
     */
    protected User findUserIfExist(String token) {
        LOG.info("Init Find user if exist.");
        var login = getClaim(Claim.LOGIN, token);
        var code = getClaim(Claim.CODE, token);
        var user = userQueryOut.getUserByProjection(null, login, code);
        if (ObjectUtils.isEmpty(user)) {
            LOG.warn("{} # {} - Login and code not found.", login, code);
            throw GlobalException.builder()
                    .status(404)
                    .alert(new CustomAlert(SystemCodeEnum.C051PI))
                    .build();
        }
        LOG.info("End Find user if exist.");
        user.setPassword("***********");
        return user;
    }

    /**
     * Retrieves a claim from the provided JWT token.
     * <p>
     *     This method takes a claim name and a JWT token string, decodes the token,
     *     and returns the claim value. If the token is invalid or the claim does not
     *     exist, a {@link GlobalException} is thrown.
     * </p>
     * @param claim the name of the claim to retrieve.
     * @param token the JWT token string containing the claim.
     * @return the claim value if the claim exists.
     * @throws GlobalException if the token is invalid or the claim does not exist.
     */
    protected String getClaim(String claim, String token) {
        LOG.info("Init Get claim: {}", claim);
        try {
            var onlyToken = Utils.removePrefixBearerToToken(token);
            var jwt = jwtDecoder.decode(onlyToken);
            return jwt.getClaim(claim);
        } catch (JwtException e) {
            LOG.error("Token is invalid.");
            throw GlobalException.builder()
                    .status(422)
                    .alert(new CustomAlert(SystemCodeEnum.C060PI))
                    .build();
        }
    }

    /**
     * Checks if the user's email is already in use during an update process.
     * <p>
     *     This method takes an email address and checks if a user with the same email
     *     address already exists. If it does, a {@link GlobalException} is thrown.
     * </p>
     * @param email the email address to check.
     * @throws GlobalException if a user with the same email address already exists.
     */
    protected void ifUserUpdateEmailCheck(String email) throws GlobalException {
        LOG.info("Init If user update email check.");
        if (!ObjectUtils.isEmpty(email)) {
            var user = userQueryOut.getUserByProjection(email, null, null);
            if (!ObjectUtils.isEmpty(user)) {
                LOG.warn("{} - Email already in use.", email);
                throw GlobalException.builder()
                        .status(409)
                        .alert(new CustomAlert(SystemCodeEnum.C052PI))
                        .build();
            }
        }
    }

    /**
     * Checks if the user's old password matches the stored password during an update process.
     * <p>
     *     This method takes a user entity and an old password, and checks if the old password
     *     matches the stored password. If it doesn't, a {@link GlobalException} is thrown.
     * </p>
     * @param user the user entity to check.
     * @param oldPassword the old password to check.
     * @throws GlobalException if the old password does not match the stored password.
     */
    protected void ifUserUpdatePasswordCheck(User user, String oldPassword) {
        LOG.info("Init If user update password check.");
        if (!ObjectUtils.isEmpty(oldPassword)) {
            if (!Crypt.isMatch(oldPassword, user.getPassword())) {
                LOG.warn("Old password does not match.");
                throw GlobalException.builder()
                        .status(409)
                        .alert(new CustomAlert(SystemCodeEnum.CO54PI))
                        .build();
            }
        }
    }

    /**
     * Encrypts the user's new password if it is provided.
     * <p>
     *     This method checks if the new password is not empty and encrypts it using
     *     the {@link Crypt} utility class. If the password is empty, it returns null.
     * </p>
     * @return the encrypted password or null if the password is empty.
     */
    private String ifUserUpdatePasswordGet() {
        return ObjectUtils.isEmpty(dto.password()) ? null : Crypt.encrypt(dto.password());
    }
}
