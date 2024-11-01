package org.hubert.common.demo.service;

import lombok.RequiredArgsConstructor;
import org.hubert.common.demo.entity.RefreshToken;
import org.hubert.common.demo.entity.User;
import org.hubert.common.demo.enums.ResponseEnum;
import org.hubert.common.demo.exceptions.CustomException;
import org.hubert.common.demo.exceptions.TokenRefreshException;
import org.hubert.common.demo.locks.DistributedLock;
import org.hubert.common.demo.properties.RedisKeyProperties;
import org.hubert.common.demo.repository.RefreshTokenRepository;
import org.hubert.common.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Provides services related to the management of refresh tokens, such as creation, retrieval, and verification.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/29 23:26
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration-ms:2592000000}")
    private Long refreshTokenDurationMs;

    @Value("${jwt.generate-times:5}")
    private Integer retryTime;

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserRepository userRepository;

    private final DistributedLock distributedLock;

    private final RedisKeyProperties redisKeyProperties;

    /**
     * Creates a new refresh token for the specified user.
     * <p>
     * This method generates a unique refresh token for the given user ID and saves it in the repository.
     * If a unique token cannot be generated after multiple attempts, a CustomException is thrown.
     *
     * @param userId the ID of the user for whom the refresh token is to be created
     * @return the created RefreshToken
     * @throws CustomException if the user is not found or if a unique refresh token cannot be created
     */
    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ResponseEnum.USER_NOT_FOUND));
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpireDate(Instant.now().plusMillis(refreshTokenDurationMs));
        boolean tokenSaved = false;
        for (int retryCount = 0; retryCount < retryTime; retryCount++) {
            String token = UUID.randomUUID().toString();
            refreshToken.setToken(token);
            String lockKey = redisKeyProperties.getRefreshTokenKey() + token;
            tokenSaved = distributedLock.tryLock(lockKey, () -> {
                try {
                    refreshTokenRepository.save(refreshToken);
                    return true;
                } catch (DataIntegrityViolationException e) {
                    return false;
                }
            });
            if (tokenSaved) {
                break;
            }
        }
        if (!tokenSaved) {
            throw new CustomException(ResponseEnum.INTERNAL_SERVER_ERROR.getCode(),
                    "Unable to create a unique refresh token after multiple attempts.");
        }
        return refreshToken;
    }

    /**
     * Finds and returns a RefreshToken based on the provided token string.
     *
     * @param token the token string used to find the RefreshToken
     * @return an Optional containing the found RefreshToken, or an empty Optional if no RefreshToken was found
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Verifies whether a given refresh token has expired and deletes it if it has.
     *
     * @param refreshToken the RefreshToken to be verified
     * @return the provided RefreshToken if it has not expired
     * @throws TokenRefreshException if the refresh token has expired
     */
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpireDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenRefreshException(refreshToken.getToken(),
                    "Refresh token was expired. Pleash make a new signin request");
        }
        return refreshToken;
    }
}
