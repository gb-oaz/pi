package com.pi.core_auth.core.dtos;

import com.pi.core_auth.core.enums.CommandType;
import com.pi.core_auth.core.utils.constants.Command;
import com.pi.core_auth.core.utils.validations.Validate;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.interfaces.IGValidationDto;
import com.pi.utils.models.CustomAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public record CommandDto(
        CommandType commandType,
        String login,
        String code,
        String password,
        String anonymousToken
) implements IGValidationDto<Map<String, Object>> {
    private static final Logger LOG = LoggerFactory.getLogger(CommandDto.class);
    private static final String LOG_MESSAGE_FORMAT = "CommandDto - {} - : {}.";

    @Override
    public Map<String, Object> validate() throws GlobalException {
        LOG.info(LOG_MESSAGE_FORMAT, commandType, this);
        Validate.command(commandType.toString());

        return switch (commandType) {
            case POST_SIGN_IN_TOKEN -> {
                Validate.login(login);
                Validate.code(code);
                Validate.password(password);
                Validate.token(anonymousToken);
                yield Map.of(
                        Command.LOGIN, login,
                        Command.CODE, code,
                        Command.PASSWORD, password,
                        Command.ANONYMOUS_TOKEN, anonymousToken
                );
            }
            case POST_ANONYMOUS_TOKEN -> {
                yield  Map.of();
            }
            default -> {
                LOG.error("{} - Invalid command type.", SystemCodeEnum.C030PI.name());
                throw GlobalException.builder()
                        .status(400)
                        .alert(new CustomAlert(SystemCodeEnum.C030PI))
                        .build();
            }
        };
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CommandType commandType;
        private String login;
        private String code;
        private String password;
        private String anonymousToken;

        public Builder commandType(CommandType commandType) {
            this.commandType = commandType;
            return this;
        }

        public Builder login(String login) {
            this.login = login;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder anonymousToken(String anonymousToken) {
            this.anonymousToken = anonymousToken;
            return this;
        }

        public CommandDto build() {
            return new CommandDto(commandType, login, code, password, anonymousToken);
        }
    }
}
