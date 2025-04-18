package com.pi.core_auth.core.utils.validations;

import com.pi.core_auth.core.enums.QueryType;
import com.pi.core_auth.core.enums.CommandType;
import com.pi.core_auth.core.utils.constants.Regex;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import org.springframework.util.ObjectUtils;

import java.util.EnumSet;

/**
 * Utility class for performing validations on various fields.
 * Provides methods to validate query and token fields, ensuring they meet specific criteria.
 */
public class Validate {
    public static void query(String query) {
        if (ObjectUtils.isEmpty(query) || EnumSet.allOf(QueryType.class).stream().noneMatch(q -> q.name().equals(query))) {
            throw GlobalException.builder()
                .status(400)
                .alert(new CustomAlert(SystemCodeEnum.C010PI))
                .build();
        }
    }

    public static void token(String token) {
        if (ObjectUtils.isEmpty(token)) {
            throw GlobalException.builder()
                .status(400)
                .alert(new CustomAlert(SystemCodeEnum.C011PI))
                .build();
        }
    }

    public static void command(String command) {
        if (ObjectUtils.isEmpty(command) || EnumSet.allOf(CommandType.class).stream().noneMatch(c -> c.name().equals(command))) {
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C030PI))
                    .build();
        }
    }

    public static void login(String login) {
        if (ObjectUtils.isEmpty(login) || !login.matches(Regex.LOGIN)) {
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C031PI))
                    .build();
        }
    }

    public static void code(String code) {
        if (ObjectUtils.isEmpty(code) || !code.matches(Regex.CODE)) {
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C032PI))
                    .build();
        }
    }

    public static void password(String password) {
        if (ObjectUtils.isEmpty(password) || !password.matches(Regex.PASSWORD)) {
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C033PI))
                    .build();
        }
    }
}
