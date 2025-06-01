package com.pi.unitary.core_auth.core.domains;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.pi.core_auth.core.domains.Token;
import com.pi.core_auth.core.enums.StatusType;

class TokenTest {

    @Test
    void builderCreatesTokenWithAllFieldsSet() {
        Token token = Token.builder()
                .token("Bearer example-token")
                .createAt("2024-09-23T10:00:00Z")
                .expiryAt("2024-09-24T10:00:00Z")
                .status(StatusType.ACTIVE)
                .build();

        Assertions.assertEquals("Bearer example-token", token.getToken());
        Assertions.assertEquals("2024-09-23T10:00:00Z", token.getCreateAt());
        Assertions.assertEquals("2024-09-24T10:00:00Z", token.getExpiryAt());
        Assertions.assertEquals(StatusType.ACTIVE, token.getStatus());
    }

    @Test
    void builderCreatesTokenWithNullFields() {
        Token token = Token.builder().build();

        Assertions.assertNull(token.getToken());
        Assertions.assertNull(token.getCreateAt());
        Assertions.assertNull(token.getExpiryAt());
        Assertions.assertNull(token.getStatus());
    }

    @Test
    void builderHandlesEmptyTokenString() {
        Token token = Token.builder()
                .token("")
                .createAt("2024-09-23T10:00:00Z")
                .expiryAt("2024-09-24T10:00:00Z")
                .status(StatusType.ACTIVE)
                .build();

        Assertions.assertEquals("", token.getToken());
        Assertions.assertEquals("2024-09-23T10:00:00Z", token.getCreateAt());
        Assertions.assertEquals("2024-09-24T10:00:00Z", token.getExpiryAt());
        Assertions.assertEquals(StatusType.ACTIVE, token.getStatus());
    }

    @Test
    void builderHandlesNullStatusType() {
        Token token = Token.builder()
                .token("Bearer example-token")
                .createAt("2024-09-23T10:00:00Z")
                .expiryAt("2024-09-24T10:00:00Z")
                .status(null)
                .build();

        Assertions.assertEquals("Bearer example-token", token.getToken());
        Assertions.assertEquals("2024-09-23T10:00:00Z", token.getCreateAt());
        Assertions.assertEquals("2024-09-24T10:00:00Z", token.getExpiryAt());
        Assertions.assertNull(token.getStatus());
    }

    @Test
    void builderHandlesInvalidDateFormat() {
        Token token = Token.builder()
                .token("Bearer example-token")
                .createAt("invalid-date")
                .expiryAt("invalid-date")
                .status(StatusType.ACTIVE)
                .build();

        Assertions.assertEquals("Bearer example-token", token.getToken());
        Assertions.assertEquals("invalid-date", token.getCreateAt());
        Assertions.assertEquals("invalid-date", token.getExpiryAt());
        Assertions.assertEquals(StatusType.ACTIVE, token.getStatus());
    }
}