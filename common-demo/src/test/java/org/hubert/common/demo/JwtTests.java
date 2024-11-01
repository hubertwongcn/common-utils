package org.hubert.common.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.hubert.common.demo.security.JwtTokenProvider;
import org.hubert.common.demo.service.impl.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/30 17:10
 */
@SpringBootTest
public class JwtTests {
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtTests(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testTokenCreatingAndExpire() {
        // Mock Authentication and UserDetails
        Authentication authentication = Mockito.mock(Authentication.class);
        UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        // Generate token
        String jwt = jwtTokenProvider.generateToken(authentication);
        // 手动解析 JWT 的 payload 部分
        String[] parts = jwt.split("\\.");
        String payload = new String(Base64.getUrlDecoder().decode(parts[1]));

        // 使用 Jackson 解析 payload JSON 内容
        ObjectMapper mapper = new ObjectMapper();
        Claims claims = null;
        try {
            claims = mapper.readValue(payload, DefaultClaims.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verify token claims
        assertEquals("testUser", claims.getSubject());
        assertTrue(claims.getExpiration().after(new Date()));

        // Print expiration date for debugging
        System.out.println("Token Expiry Date: " + claims.getExpiration());

        // Check expiration time within expected bounds
        long expectedExpirationTime = System.currentTimeMillis() + 7200000; // assuming 2 hour expiration for example
        long actualExpirationTime = claims.getExpiration().getTime();
        assertTrue(actualExpirationTime <= expectedExpirationTime + 1000); // Allowing 1 sec leeway
        assertTrue(actualExpirationTime >= expectedExpirationTime - 1000);
    }
}
