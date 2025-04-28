package com.pi.core_live.core.utils.validations;

import com.pi.core_live.core.enums.CommandType;
import com.pi.core_live.core.enums.QueryType;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;
import com.pi.utils.validations.ValidationCommon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

import java.util.EnumSet;
import java.util.List;

/**
 * Utility class for performing validations on various fields.
 * Provides methods to validate query and token fields, ensuring they meet specific criteria.
 */
public class Validate extends ValidationCommon {
    private static final Logger LOG = LoggerFactory.getLogger(Validate.class);

    public static void query(String query) {
        LOG.info("Init auth validate query format: {}", query);
        if (ObjectUtils.isEmpty(query) || EnumSet.allOf(QueryType.class).stream().noneMatch(q -> q.name().equals(query))) {
            LOG.warn("{} - Invalid query type.", SystemCodeEnum.C010PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C010PI))
                    .build();
        }
    }

    public static void command(String command) {
        LOG.info("Init auth validate command format: {}", command);
        if (ObjectUtils.isEmpty(command) || EnumSet.allOf(CommandType.class).stream().noneMatch(c -> c.name().equals(command))) {
            LOG.warn("{} - Invalid command type.", SystemCodeEnum.C030PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C030PI))
                    .build();
        }
    }

    public static void key(String key) {
        LOG.info("Init validate key format: {}", key);
        if (ObjectUtils.isEmpty(key)) {
            LOG.warn("{} - Invalid key type.", SystemCodeEnum.C101PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C101PI))
                    .build();
        }
    }

    public static void answerItem(List<String> answerItem) {
        LOG.info("Init validate answerItem format: {}", answerItem);
        if (ObjectUtils.isEmpty(answerItem)) {
            LOG.warn("{} - Invalid answerItem type.", SystemCodeEnum.C102PI.name());
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C102PI))
                    .build();
        }
    }
}
