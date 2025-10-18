package com.example.erp.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    @Test
    void failsWhenSecretTooShort() {
        JwtTokenProvider provider = new JwtTokenProvider();
        ReflectionTestUtils.setField(provider, "secret", "short-secret");
        ReflectionTestUtils.setField(provider, "expirationMs", 3600_000L);
        assertThrows(IllegalStateException.class,
            () -> provider.generateToken("user", List.of("ROLE_USER")));
    }

    @Test
    void generatesTokenWithRoles() {
        JwtTokenProvider provider = new JwtTokenProvider();
        ReflectionTestUtils.setField(provider, "secret", "test-secret-0123456789-0123456789-abcdef");
        ReflectionTestUtils.setField(provider, "expirationMs", 3600_000L);

        String token = provider.generateToken("alice", List.of("ROLE_USER", "ROLE_ADMIN"));
        assertNotNull(token);
        assertEquals("alice", provider.getUsername(token));
        assertFalse(provider.isExpired(token));
        assertTrue(provider.getRoles(token).contains("ROLE_ADMIN"));
    }
}

