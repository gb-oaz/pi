package com.pi.core_user.core.dtos;

import com.pi.core_user.core.utils.validations.Validate;
import com.pi.core_user.core.domains.User;
import com.pi.core_user.core.enums.QueryType;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.interfaces.IGValidationDto;
import com.pi.utils.models.CustomAlert;

import com.pi.utils.services.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public record QueryDto(
        String queryType,
        String token,
        String name,
        String email,
        String login,
        String code,
        Integer page,
        Integer size
) implements IGValidationDto<User> {
    private static final Logger LOG = LoggerFactory.getLogger(QueryDto.class);
    private static final String LOG_MESSAGE_FORMAT = "QueryDto - {} - : {}.";

    @Override
    public User validate() throws GlobalException {
        LOG.info(LOG_MESSAGE_FORMAT, queryType, this);
        Validate.query(queryType.toString());
        Validate.token(token);
        if (!ObjectUtils.isEmpty(email)) Validate.email(email);
        if (!ObjectUtils.isEmpty(login)) Validate.login(login);
        if (!ObjectUtils.isEmpty(code)) Validate.code(code);

        return switch (Utils.ifEnumGet(queryType, QueryType.class)) {
            case QUERY_GET_USER_BY_PROJECTION -> {
                yield User.builder()
                        .name(name)
                        .email(email)
                        .login(login)
                        .code(code)
                        .build();
            }
            case QUERY_GET_USERS_BY_PROJECTION -> {
                if (!ObjectUtils.isEmpty(name)) Validate.name(name);
                yield User.builder()
                        .name(name)
                        .email(email)
                        .login(login)
                        .code(code)
                        .build();
            }
            default -> {
                LOG.error("{} - Invalid query type.", SystemCodeEnum.C070PI.name());
                throw GlobalException.builder()
                        .status(400)
                        .alert(new CustomAlert(SystemCodeEnum.C070PI))
                        .build();
            }
        };
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String queryType;
        private String token;
        private String name;
        private String email;
        private String login;
        private String code;
        private Integer page;
        private Integer size;

        public Builder queryType(String queryType) { this.queryType = queryType; return this; }
        public Builder token(String token) { this.token = token; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder login(String login) { this.login = login; return this; }
        public Builder code(String code) { this.code = code; return this; }
        public Builder page(Integer page) { this.page = page; return this; }
        public Builder size(Integer size) { this.size = size; return this; }

        public QueryDto build() {
            return new QueryDto(queryType, token, name, email, login, code, page, size);
        }
    }
}
