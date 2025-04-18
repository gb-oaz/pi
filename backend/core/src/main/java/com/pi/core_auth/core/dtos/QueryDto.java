package com.pi.core_auth.core.dtos;

import com.pi.core_auth.core.domains.Token;
import com.pi.core_auth.core.enums.QueryType;
import com.pi.core_auth.core.utils.validations.Validate;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.interfaces.IGValidationDto;
import com.pi.utils.models.CustomAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record QueryDto(
        QueryType queryType,
        String token
) implements IGValidationDto<Token> {
    private static final Logger LOG = LoggerFactory.getLogger(QueryDto.class);
    private static final String LOG_MESSAGE_FORMAT = "QueryDto - {} - : {}.";

    @Override
    public Token validate() throws GlobalException {
        LOG.info(LOG_MESSAGE_FORMAT, queryType, this);
        Validate.query(queryType.toString());

        return switch (queryType) {
            case GET_STATUS_TOKEN, GET_SCOPE_TOKEN -> {
                Validate.token(token);
                yield Token.builder()
                    .token(token)
                    .build();
            }
            default -> {
                LOG.error("{} - Invalid query type.", SystemCodeEnum.C010PI.name());
                throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C010PI))
                    .build();
            }
        };
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private QueryType queryType;
        private String token;

        public Builder queryType(QueryType queryType) {
            this.queryType = queryType;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public QueryDto build() {
            return new QueryDto(queryType, token);
        }
    }
}
