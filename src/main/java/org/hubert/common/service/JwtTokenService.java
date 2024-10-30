package org.hubert.common.service;

import lombok.RequiredArgsConstructor;
import org.hubert.common.properties.RedisKeyProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Service class for managing JWT tokens associated with users.
 * This service uses Redis as the underlying storage mechanism
 * to store and retrieve JWT tokens for users.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/30 22:00
 */
@Service
@RequiredArgsConstructor
public class JwtTokenService {
    /**
     * An instance of RedisTemplate used to interact with Redis for storing
     * and retrieving JWT tokens associated with users.
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Holds configuration properties for Redis key prefixes.
     * These properties are used to construct keys for storing
     * and retrieving data in Redis.
     */
    private final RedisKeyProperties redisKeyProperties;

    /**
     * Updates the current JWT token for the specified user in Redis with an expiration time.
     *
     * @param username     the username for which the JWT should be updated
     * @param token        the JWT token to be stored
     * @param expireTimeMs the expiration time in milliseconds for the token
     */
    public void updateCurrentJwt(String username, String token, long expireTimeMs) {
        String key = redisKeyProperties.getUserJwtKey() + username;
        redisTemplate.opsForValue().set(key, token, expireTimeMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Retrieves the current JWT token associated with the specified username from Redis.
     *
     * @param username the username for which the JWT token should be retrieved
     * @return the JWT token associated with the specified username, or null if not found
     */
    public String getCurrentJwt(String username) {
        String key = redisKeyProperties.getUserJwtKey() + username;
        return (String) redisTemplate.opsForValue().get(key);
    }
}
