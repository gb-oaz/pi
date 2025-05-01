package com.pi.core_auth.core.utils.interfaces;

import com.pi.core_auth.core.dtos.QueryDto;
import com.pi.core_auth.core.enums.QueryType;
import com.pi.core_auth.usecases.CaseGetScopeToken;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;

import org.springframework.security.oauth2.jwt.JwtDecoder;

/**
 * Interface for checking token credentials.
 *
 * @author Gustavo Boaz
 */
public interface ITokenCheck {

    /**
     * Checks that the credentials are valid.
     *
     * <p>This method creates an instance of {@link CaseGetScopeToken} and calls it to obtain the
     * teacher's login and code. It then calls the {@link #checkLogin(String, String)} and
     * {@link #checkCode(String, String)} methods to verify that the credentials are valid.
     *
     * @param jwtDecoder the JwtDecoder to use for validating the JWT token.
     * @param token the JWT token to validate.
     * @param login the login
     * @param code the code
     *
     * @throws GlobalException if the credentials are invalid.
     */
    default void checkCredentials(JwtDecoder jwtDecoder, String token, String login, String code) throws GlobalException {
        var useCase = new CaseGetScopeToken(jwtDecoder);
        var caseDto = QueryDto.builder()
                .queryType(QueryType.QUERY_GET_SCOPE_TOKEN.name())
                .token(token)
                .build();
        useCase.setQueryDto(caseDto);
        var response = useCase.call();
        checkLogin(response.login, login);
        checkCode(response.code, code);
    }

    /**
     * Checks that the login is valid.
     *
     * <p>This method verifies that login matches the one provided in token<p/>
     *
     * @param currentLogin the login of the teacher
     * @param login the login of the teacher
     *
     * @throws GlobalException if the login is invalid
     */
    default void checkLogin(String currentLogin, String login) {
        if (!currentLogin.equals(login))
            throw GlobalException.builder()
                    .status(403)
                    .details("Login is not match token")
                    .alert(new CustomAlert(SystemCodeEnum.C003PI))
                    .build();
    }

    /**
     * Checks that the code is valid.
     *
     * <p>This method verifies that code matches the one provided in token<p/>
     *
     * @param currentCode the code of the teacher
     * @param code the code of the teacher
     *
     * @throws GlobalException if the code is invalid
     */
    default void checkCode(String currentCode, String code) {
        if (!currentCode.equals(code))
            throw GlobalException.builder()
                    .status(403)
                    .details("Code is not match token")
                    .alert(new CustomAlert(SystemCodeEnum.C003PI))
                    .build();
    }
}
