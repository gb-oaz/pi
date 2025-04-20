package com.pi.core_user.core.dtos;

import com.pi.core_auth.core.enums.ScopeType;
import com.pi.core_user.core.domains.User;
import com.pi.core_user.core.enums.CommandType;
import com.pi.core_user.core.utils.validations.Validate;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.interfaces.IGValidationDto;
import com.pi.utils.models.CustomAlert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.EnumSet;

public record CommandDto(
        CommandType commandType,
        String token,
        String name,
        String email,
        String login,
        String code,
        String password,
        String oldPassword
) implements IGValidationDto<User> {
    private static final Logger LOG = LoggerFactory.getLogger(CommandDto.class);
    private static final String LOG_MESSAGE_FORMAT = "CommandDto - {} - : {}.";

    @Override
    public User validate() throws GlobalException {
        LOG.info(LOG_MESSAGE_FORMAT, commandType, this);
        // Common validations
        Validate.command(commandType.toString());
        Validate.token(token);

        return switch (commandType) {
            case COMMAND_POST_CREATE_USER_TEACHER -> {
                Validate.name(name);
                Validate.email(email);
                Validate.login(login);
                Validate.code(code);
                Validate.password(password);
                yield User.builder()
                        .name(name)
                        .email(email)
                        .login(login)
                        .code(code)
                        .password(password)
                        .scopes(EnumSet.of(ScopeType.TEACHER))
                        .build();
            }
            case COMMAND_POST_CREATE_USER_STUDENT -> {
                Validate.name(name);
                Validate.email(email);
                Validate.login(login);
                Validate.code(code);
                Validate.password(password);
                yield User.builder()
                        .name(name)
                        .email(email)
                        .login(login)
                        .code(code)
                        .password(password)
                        .scopes(EnumSet.of(ScopeType.STUDENT))
                        .build();
            }
            case COMMAND_PUT_UPDATE_USER -> {
                if (!ObjectUtils.isEmpty(name)) Validate.name(name);
                if (!ObjectUtils.isEmpty(email)) Validate.email(email);
                if (!ObjectUtils.isEmpty(password)) {
                    Validate.password(password);
                    Validate.password(oldPassword);
                }
                yield User.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .build();
            }
            default -> {
                LOG.error("{} - Invalid command type.", SystemCodeEnum.C080PI.name());
                throw GlobalException.builder()
                        .status(400)
                        .alert(new CustomAlert(SystemCodeEnum.C080PI))
                        .build();
            }
        };
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private CommandType commandType;
        private String token;
        private String name;
        private String email;
        private String login;
        private String code;
        private String password;
        private String oldPassword;

        public Builder commandType(CommandType commandType) { this.commandType = commandType; return this; }
        public Builder token(String anonymousToken) { this.token = anonymousToken; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder login(String login) { this.login = login; return this; }
        public Builder code(String code) { this.code = code; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder oldPassword(String oldPassword) { this.oldPassword = oldPassword; return this; }

        public CommandDto build() {
            return new CommandDto(commandType, token, name, email, login, code, password, oldPassword);
        }
    }
}
