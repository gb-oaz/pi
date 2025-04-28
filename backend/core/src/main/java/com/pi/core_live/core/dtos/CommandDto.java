package com.pi.core_live.core.dtos;

import com.pi.core_live.core.enums.CommandType;
import com.pi.core_live.core.utils.validations.Validate;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.interfaces.IGValidationDto;
import com.pi.utils.models.CustomAlert;
import com.pi.utils.services.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public record CommandDto(
        String commandType,
        String token,
        String login,
        String code,
        String keyQuiz,
        String keyLive,
        String pupilLogin,
        String pupilCode,
        List<String> answerItem
) implements IGValidationDto<Void> {
    private static final Logger LOG = LoggerFactory.getLogger(CommandDto.class);
    private static final String LOG_MESSAGE_FORMAT = "CommandDto - {} - : {}.";

    @Override
    public Void validate() throws GlobalException {
        LOG.info(LOG_MESSAGE_FORMAT, commandType, this);
        Validate.command(commandType);
        Validate.token(token);

        return switch (Utils.ifEnumGet(commandType, CommandType.class)) {
            case COMMAND_POST_NEW_LIVE -> {
                Validate.login(login);
                Validate.code(code);
                Validate.key(keyQuiz);
                yield null;
            }
            case COMMAND_PATCH_NEXT_POSITION, COMMAND_PATCH_PREVIOUS_POSITION, COMMAND_PATCH_END_LIVE -> {
                Validate.login(login);
                Validate.code(code);
                Validate.key(keyLive);
                yield null;
            }
            case COMMAND_PATCH_REMOVE_PUPIL_FROM_LOBBY -> {
                Validate.login(login);
                Validate.code(code);
                Validate.key(keyLive);
                Validate.login(pupilLogin);
                Validate.code(pupilCode);
                yield null;
            }
            case COMMAND_PATCH_ADD_PUPIL_TO_LOBBY -> {
                Validate.key(keyLive);
                Validate.login(pupilLogin);
                Validate.code(pupilCode);
                yield null;
            }
            case COMMAND_PATCH_ADD_PUPIL_ANSWER_TO_QUIZ -> {
                Validate.login(pupilLogin);
                Validate.code(pupilCode);
                Validate.key(keyLive);
                Validate.answerItem(answerItem);
                yield null;
            }
            default -> {
                LOG.error("{} - Invalid command type.", SystemCodeEnum.C130PI.name());
                throw GlobalException.builder()
                        .status(400)
                        .alert(new CustomAlert(SystemCodeEnum.C130PI))
                        .build();
            }
        };
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String commandType;
        private String token;
        private String login;
        private String code;
        private String keyQuiz;
        private String keyLive;
        private String pupilLogin;
        private String pupilCode;
        private List<String> answerItem;

        public Builder commandType(String commandType) { this.commandType = commandType; return this; }
        public Builder token(String token) { this.token = token; return this; }
        public Builder login(String login) { this.login = login; return this; }
        public Builder code(String code) { this.code = code; return this; }
        public Builder keyQuiz(String keyQuiz) { this.keyQuiz = keyQuiz; return this; }
        public Builder keyLive(String keyLive) { this.keyLive = keyLive; return this; }
        public Builder pupilLogin(String pupilLogin) { this.pupilLogin = pupilLogin; return this; }
        public Builder pupilCode(String pupilCode) { this.pupilCode = pupilCode; return this; }
        public Builder answerItem(List<String> answerItem) { this.answerItem = answerItem; return this; }
        public CommandDto build() { return new CommandDto(commandType, token, login, code, keyQuiz, keyLive, pupilLogin, pupilCode, answerItem); }
    }
}
