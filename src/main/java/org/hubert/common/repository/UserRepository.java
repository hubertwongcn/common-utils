package org.hubert.common.repository;

import org.hubert.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/29 22:56
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a User entity by its unique username.
     *
     * @param username the unique username to search for
     * @return an Optional containing the found User, or empty if no user is found
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user with the specified username exists in the repository.
     *
     * @param username the unique username to check for existence
     * @return true if a user with the specified username exists, false otherwise
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a user with the specified email exists in the repository.
     *
     * @param email the email to check for existence
     * @return true if a user with the specified email exists, false otherwise
     */
    Boolean existsByEmail(String email);
}
