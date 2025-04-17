package com.pi.core_auth.core.dtos;

import com.pi.core_auth.core.domains.Token;
import com.pi.core_auth.core.enums.QueryType;
import com.pi.core_auth.core.utils.validations.Validate;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.interfaces.IGValidationDto;
import com.pi.utils.models.CustomAlert;

public record QueryDto(
        QueryType queryType,
        String token
) implements IGValidationDto<Token> {
    @Override
    public Token validate() throws GlobalException {
        Validate.query(queryType.toString());

        return switch (queryType) {
            case GET_STATUS_TOKEN -> {
                Validate.token(token);
                yield Token.builder()
                    .token(token)
                    .build();
            }
            default -> {
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
