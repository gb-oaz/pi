package com.pi.utils.validations;

import com.pi.utils.constants.Regex;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public class ValidationCommon {
    private static final Logger LOG = LoggerFactory.getLogger(ValidationCommon.class);

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

    public static void token(String token) {
        LOG.info("Init validate token format: {}", token);
        if (ObjectUtils.isEmpty(token)) {
            LOG.warn("{} - Invalid token type.", SystemCodeEnum.C121PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C121PI))
                    .build();
        }
    }
}
