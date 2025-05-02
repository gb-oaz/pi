package com.pi.utils.services;

import com.pi.core_auth.core.enums.StatusType;
import com.pi.core_auth.core.utils.models.Response;
import com.pi.utils.enums.SystemCodeEnum;
import com.pi.utils.exceptions.GlobalException;
import com.pi.utils.models.CustomAlert;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.JwtException;

import java.security.SecureRandom;
import java.time.Instant;

public class Utils {
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
    private static final String PUPIL = "PUPIL";

    /**
     * SecureRandom instance for generating random numbers.
     */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Removes the "Bearer " prefix from the token if present.
     *
     * @param token the JWT token string
     * @return the token without the "Bearer " prefix
     */
    public static String removePrefixBearerToToken(String token) {
        if (token.startsWith("Bearer ")) {
            LOG.info("Removing 'Bearer ' prefix from token: {}", token);
            return token.substring(7);
        }
        return token;
    }

    /**
     * Validates the JWT token and handles exceptions.
     *
     * @param exception the JwtException thrown during token validation
     * @throws GlobalException if the token is invalid or expired
     */
    public static Response validateResponseJwtException(JwtException exception) {
        LOG.warn("Error decoding token: {}", exception.getMessage());
        if (exception.getMessage().contains("Expired")) {
            LOG.info("Token is expired.");
            return Response.builder()
                    .status(StatusType.EXPIRED)
                    .build();
        } else if (exception.getMessage().contains("Invalid")) {
            LOG.info("Token is invalid.");
            return Response.builder()
                    .status(StatusType.INVALID)
                    .build();
        } else {
            LOG.error("Token is invalid.");
            throw GlobalException.builder()
                    .status(422)
                    .alert(new CustomAlert(SystemCodeEnum.C060PI))
                    .build();
        }
    }

    /**
     * Generates a token using the current timestamp and a random number.
     *
     * @return the generated token. ex - 17f9343c5c114ed6a
     */
    public static String generateRandomToken() {
        LOG.info("Generate token session.");
        long currentTimeMillis = System.currentTimeMillis();
        int randomInt = SECURE_RANDOM.nextInt();
        return Long.toHexString(currentTimeMillis) + Integer.toHexString(randomInt);
    }

    /**
     * Generates a string in the format PUPILXXX where X are random letters.
     * @return the generated string. ex - PUPILABC
     */
    public static String generatePupilRandom() {
        LOG.info("Generate PUPIL with random letters.");
        StringBuilder sb = new StringBuilder(PUPIL);

        for (int i = 0; i < 3; i++) {
            char randomChar = (char) (SECURE_RANDOM.nextInt(26) + 65);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    /**
     * Generates a random 6-digit number from 000000 to 999999.
     * @return the generated number as String with leading zeros. ex - "004567", "123456", "000001"
     */
    public static String generateRandom6Digits() {
        LOG.info("Generate 6-digit random number with leading zeros.");
        int randomNum = SECURE_RANDOM.nextInt(1000000);

        return String.format("%06d", randomNum);
    }
    /**
     * Verifies if a value is part of an Enum.
     *
     * @param value    the value to be checked
     * @param enumClass the Enum class
     * @return true if the value is part of the Enum, false otherwise
     */
    public static <T extends Enum<T>> boolean isInEnum(String value, Class<T> enumClass) {
        try {
            Enum.valueOf(enumClass, value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Verifies if a value is part of an Enum return enum.
     *
     * @param value     the value to be checked
     * @param enumClass the Enum class
     * @param <T>       the value enum
     * @return T        entity
     * @throws GlobalException in case error
     */
    public static <T extends Enum<T>> T ifEnumGet(String value, Class<T> enumClass) throws GlobalException {
        try {
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException e) {
            throw GlobalException.builder()
                    .status(400)
                    .alert(new CustomAlert(SystemCodeEnum.C003PI))
                    .details("Check value enum: " + value)
                    .build();
        }
    }

    /**
     * Returns the current time in the ISO-8601 format.
     *
     * @return the current time as a string
     */
    public static String now() {
        return Instant.now().toString();
    }
}
