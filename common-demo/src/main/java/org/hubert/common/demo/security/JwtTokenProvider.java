package org.hubert.common.demo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecureDigestAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hubert.common.demo.service.impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

/**
 * Provides utilities for generating, parsing, and validating JWT (JSON Web Tokens).
 * This component utilizes a secret key for token encryption and decryption.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/29 23:11
 */
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    /**
     * Secret key used for JWT (JSON Web Token) encryption and decryption.
     * This value is injected from the application properties using the key "jwt.secret".
     * Make sure that keys used with HMAC-SHA algorithms MUST have a size >= 512 bits
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Specifies the expiration time (in milliseconds) for JWT (JSON Web Tokens).
     * This value is injected from the application properties using the key "jwt.expiration-in-ms".
     */
    @Getter
    @Value("${jwt.expiration-in-ms}")
    private int jwtExpirationInMs;

    /**
     * Decodes the JWT secret key from its Base64 encoded form and returns it as a SecretKey instance.
     *
     * @return a SecretKey object derived from the Base64 encoded JWT secret.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generates a JWT (JSON Web Token) for the authenticated user.
     *
     * @param authentication the authentication object containing the user's authentication details.
     * @return a JWT token string.
     */
    public String generateToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        SecretKey key = getSigningKey();
        SecureDigestAlgorithm<? super SecretKey, ?> alg = Jwts.SIG.HS512;
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationInMs))
                .signWith(key, alg)
                .compact();
    }

    /**
     * Generates a JWT (JSON Web Token) using the provided username.
     *
     * @param username the username for which the token is to be generated.
     * @return a JWT token string associated with the given username.
     */
    public String generateTokenFromUsername(String username) {
        SecretKey key = getSigningKey();
        SecureDigestAlgorithm<? super SecretKey, ?> alg = Jwts.SIG.HS512;
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationInMs))
                .signWith(key, alg)
                .compact();
    }

    /**
     * Extracts the username from the provided JWT token.
     *
     * @param token the JWT token from which the username is to be extracted.
     * @return the username associated with the provided JWT token.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates the provided JWT token by attempting to parse its signed claims.
     *
     * @param authToken the JWT token to be validated
     * @return true if the token is valid, false otherwise
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(authToken);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
