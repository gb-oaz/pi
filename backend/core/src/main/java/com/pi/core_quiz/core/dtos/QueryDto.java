package com.pi.core_quiz.core.dtos;

import com.pi.core_quiz.core.enums.QueryType;
import com.pi.core_quiz.core.utils.validations.Validate;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.interfaces.IGValidationDto;
import com.pi.utils.models.CustomAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.Set;

public record QueryDto(
        QueryType queryType,
        String token,
        String login,
        String code,
        String key,
        Integer position,
        String name,
        Set<String> categories,
        Integer page,
        Integer size
) implements IGValidationDto<Object> {
    private static final Logger LOG = LoggerFactory.getLogger(QueryDto.class);

    @Override
    public Object validate() {
        Validate.token(token);

        return switch (queryType) {
            case QUERY_GET_QUIZ -> {
                Validate.key(key);
                yield key;
            }
            case QUERY_GET_QUIZES_PROJECTION -> {
                if (!ObjectUtils.isEmpty(login)) Validate.login(login);
                if (!ObjectUtils.isEmpty(code)) Validate.code(code);
                if (!ObjectUtils.isEmpty(name)) Validate.name(name);
                if (!ObjectUtils.isEmpty(categories)) Validate.categories(categories);
                yield token;
            }
            case QUERY_GET_QUIZ_ITEM -> {
                Validate.key(key);
                Validate.position(position);
                yield key;
            }
            default -> {
                LOG.error("{} - Invalid queryType type.", SystemCodeEnum.C110PI.name());
                throw GlobalException.builder()
                        .status(400)
                        .alert(new CustomAlert(SystemCodeEnum.C110PI))
                        .build();
            }
        };
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private QueryType queryType;
        private String token;
        private String login;
        private String code;
        private String key;
        private Integer position;
        private String name;
        private Set<String> categories;
        private Integer page;
        private Integer size;

        public Builder queryType(QueryType queryType) {
            this.queryType = queryType;
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

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder position(Integer position) {
            this.position = position;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder categories(Set<String> categories) {
            this.categories = categories;
            return this;
        }

        public Builder page(Integer page) {
            this.page = page;
            return this;
        }

        public Builder size(Integer size) {
            this.size = size;
            return this;
        }

        public QueryDto build() {
            return new QueryDto(queryType, token, login, code, key, position, name, categories, page, size);
        }
    }

}
