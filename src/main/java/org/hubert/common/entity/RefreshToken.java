package org.hubert.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.Instant;

/**
 * Represents a refresh token used for authenticating users in the system.
 * <p>
 * Each RefreshToken is associated with a unique user and contains a token string
 * along with an expiration date.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/29 22:47
 */
@Data
@Entity
@Table(name = "t_refresh_token")
public class RefreshToken {
    /**
     * The unique identifier for the RefreshToken entity.
     * This field is automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Represents the associated User entity for this RefreshToken.
     * This is a one-to-one relationship where each RefreshToken is linked to a single User.
     * The foreign key column in the "t_refresh_token" table is "user_id", which references the "id" column in the "t_user" table.
     */
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    /**
     * Represents the refresh token associated with a user.
     * This field is mandatory and must be unique across the database.
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * Represents the expiration date and time of the RefreshToken.
     * This field is mandatory and cannot be null.
     */
    @Column(nullable = false)
    private Instant expireDate;
}
