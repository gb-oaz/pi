package com.pi.core_quiz.core.dtos;

import com.pi.core_quiz.core.domain.Quiz;
import com.pi.core_quiz.core.domain.itens.IQuizItem;
import com.pi.core_quiz.core.enums.CommandType;
import com.pi.core_quiz.core.enums.QuizType;
import com.pi.core_quiz.core.enums.SlideType;
import com.pi.core_quiz.core.utils.validations.Validate;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.interfaces.IGValidationDto;
import com.pi.utils.models.CustomAlert;
import com.pi.utils.services.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Set;

public record CommandDto(
        CommandType commandType,
        String token,
        String login,
        String code,
        String name,
        String key,
        Integer position,
        String typeItem,
        IQuizItem quizItem,
        Set<String> categories
) implements IGValidationDto<Object> {
    private static final Logger LOG = LoggerFactory.getLogger(CommandDto.class);

    @Override
    public Object validate() throws GlobalException {
        Validate.token(token);

        return switch (commandType) {
            case COMMAND_POST_NEW_QUIZ -> {
                Validate.login(login);
                Validate.code(code);
                Validate.name(name);
                if (!ObjectUtils.isEmpty(categories)) Validate.categories(categories);
                yield new Quiz(login, code, name, categories);
            }
            case COMMAND_POST_QUIZ_ITEM, COMMAND_PATCH_QUIZ_ITEM -> {
                Validate.key(key);
                Validate.position(position);
                if (Utils.isInEnum(typeItem, QuizType.class)) Validate.quizType(QuizType.valueOf(typeItem), quizItem);
                if (Utils.isInEnum(typeItem, SlideType.class)) Validate.slideType(SlideType.valueOf(typeItem), quizItem);
                yield key;
            }
            case COMMAND_PUT_QUIZ -> {
                Validate.key(key);
                if (!ObjectUtils.isEmpty(name)) Validate.name(name);
                if (!ObjectUtils.isEmpty(categories)) Validate.categories(categories);
                yield key;
            }
            case COMMAND_DELETE_QUIZ_ITEM -> {
                Validate.key(key);
                Validate.position(position);
                yield key;
            }
            case COMMAND_DELETE_QUIZ -> {
                Validate.key(key);
                yield key;
            }
            default -> {
                LOG.error("{} - Invalid command type.", SystemCodeEnum.C100PI.name());
                throw GlobalException.builder()
                        .status(400)
                        .alert(new CustomAlert(SystemCodeEnum.C100PI))
                        .build();
            }
        };
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private CommandType commandType;
        private String token;
        private String login;
        private String code;
        private String name;
        private String key;
        private Integer position;
        private String typeItem;
        private IQuizItem quizItem;
        private Set<String> categories;

        public Builder commandType(CommandType commandType) {
            this.commandType = commandType;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
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

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder position(Integer position) {
            this.position = position;
            return this;
        }

        public Builder typeItem(String typeItem) {
            this.typeItem = typeItem;
            return this;
        }

        public Builder quizItem(IQuizItem quizItem) {
            this.quizItem = quizItem;
            return this;
        }

        public Builder categories(Set<String> categories) {
            this.categories = categories;
            return this;
        }

        public CommandDto build() {
            return new CommandDto(commandType, token, login, code, name, key, position, typeItem, quizItem, categories);
        }
    }
}
