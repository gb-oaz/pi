package com.pi.core_auth.core.utils.validations;

import com.pi.core_auth.core.enums.QueryType;
import com.pi.core_auth.core.enums.CommandType;
import com.pi.core_auth.core.utils.constants.Regex;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.EnumSet;

/**
 * Utility class for performing validations on various fields.
 * Provides methods to validate query and token fields, ensuring they meet specific criteria.
 */
public class Validate {
    private static final Logger LOG = LoggerFactory.getLogger(Validate.class);

    public static void query(String query) {
        LOG.info("Init validate query format: {}", query);
        if (ObjectUtils.isEmpty(query) || EnumSet.allOf(QueryType.class).stream().noneMatch(q -> q.name().equals(query))) {
            LOG.warn("{} - Invalid query type.", SystemCodeEnum.C010PI.name());
            throw GlobalException.builder()
                .status(400)
                .alert(new CustomAlert(SystemCodeEnum.C010PI))
                .build();
        }
    }

    public static void token(String token) {
        LOG.info("Init validate token format: {}", token);
        if (ObjectUtils.isEmpty(token)) {
            LOG.warn("{} - Invalid token type.", SystemCodeEnum.C011PI.name());
            throw GlobalException.builder()
                .status(400)
                .alert(new CustomAlert(SystemCodeEnum.C011PI))
                .build();
        }
    }

    public static void command(String command) {
        LOG.info("Init validate command format: {}", command);
        if (ObjectUtils.isEmpty(command) || EnumSet.allOf(CommandType.class).stream().noneMatch(c -> c.name().equals(command))) {
            LOG.warn("{} - Invalid command type.", SystemCodeEnum.C030PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C030PI))
                    .build();
        }
    }

    public static void login(String login) {
        LOG.info("Init validate login format: {}", login);
        if (ObjectUtils.isEmpty(login) || !login.matches(Regex.LOGIN)) {
            LOG.warn("{} - Invalid login type regex.", SystemCodeEnum.C031PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C031PI))
                    .build();
        }
    }

    public static void code(String code) {
        LOG.info("Init validate code format: {}", code);
        if (ObjectUtils.isEmpty(code) || !code.matches(Regex.CODE)) {
            LOG.warn("{} - Invalid code type regex.", SystemCodeEnum.C032PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C032PI))
                    .build();
        }
    }

    public static void password(String password) {
        LOG.info("Init validate password format: {}", password);
        if (ObjectUtils.isEmpty(password) || !password.matches(Regex.PASSWORD)) {
            LOG.warn("{} - Invalid password type regex.", SystemCodeEnum.C033PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C033PI))
                    .build();
        }
    }
}
