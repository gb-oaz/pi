package com.pi.core_user.core.utils.validations;

import com.pi.core_user.core.enums.CommandType;
import com.pi.core_user.core.enums.QueryType;
import com.pi.utils.constants.Regex;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import com.pi.utils.validations.ValidationCommon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.EnumSet;

public class Validate extends ValidationCommon {
    private static final Logger LOG = LoggerFactory.getLogger(Validate.class);

    public static void query(String query) {
        LOG.info("Init user validate query format: {}", query);
        if (ObjectUtils.isEmpty(query) || EnumSet.allOf(QueryType.class).stream().noneMatch(q -> q.name().equals(query))) {
            LOG.warn("{} - Invalid query type.", SystemCodeEnum.C070PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C070PI))
                    .build();
        }
    }

    public static void command(String command) {
        LOG.info("Init user validate command format: {}", command);
        if (ObjectUtils.isEmpty(command) || EnumSet.allOf(CommandType.class).stream().noneMatch(c -> c.name().equals(command))) {
            LOG.warn("{} - Invalid command type.", SystemCodeEnum.C080PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C080PI))
                    .build();
        }
    }

    public static void name(String name) {
        LOG.info("Init user validate name format: {}", name);
        if (ObjectUtils.isEmpty(name) || !name.matches(Regex.NAME)) {
            LOG.warn("{} - Invalid name format.", SystemCodeEnum.C081PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C081PI))
                    .build();
        }
    }

    public static void email(String email) {
        LOG.info("Init user validate email format: {}", email);
        if (ObjectUtils.isEmpty(email) || !email.matches(Regex.EMAIL)) {
            LOG.warn("{} - Invalid email format.", SystemCodeEnum.C082PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C082PI))
                    .build();
        }
    }
}
