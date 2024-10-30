package org.hubert.common.repository;

import org.hubert.common.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/29 22:58
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    /**
     * Finds and returns a RefreshToken based on the provided token string.
     *
     * @param token the token string used to find the RefreshToken
     * @return an Optional containing the found RefreshToken, or an empty Optional if no RefreshToken was found
     */
    Optional<RefreshToken> findByToken(String token);
}
