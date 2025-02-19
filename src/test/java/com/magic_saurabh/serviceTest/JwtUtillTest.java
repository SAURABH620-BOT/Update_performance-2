package com.magic_saurabh.serviceTest;

import com.magic_saurabh.service.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class JwtUtillTest {
    @InjectMocks
    private JwtUtil jwtUtil;

    @Mock
    private Claims claims;

    private final String secretKey = "secretKey";  // Example secret key for the tests
    private final String username = "testUser";


    @Test
    void TokenIsInvalidTest() {
        String invalidToken = "invalidToken";
        String extractedUsername = jwtUtil.extractUsername(invalidToken);
        assertNull(extractedUsername, "Username extraction should return null for invalid token");
    }
    private String createExpiredToken() {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - 1000))  // Set expiration in the past
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
