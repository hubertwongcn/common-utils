package org.hubert.common.repository;

import org.hubert.common.entity.Role;
import org.hubert.common.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for {@link Role} instances.
 * Provides basic CRUD operations due to the extension of {@link JpaRepository}.
 * Includes custom finder methods.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/29 22:54
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
    /**
     * Retrieves a {@link Role} entity based on the provided role name.
     *
     * @param name The name of the role to search for, represented as a {@link RoleEnum}.
     * @return An {@link Optional} containing the {@link Role} entity if found, or an empty {@link Optional} if not found.
     */
    Optional<Role> findByName(RoleEnum name);
}
