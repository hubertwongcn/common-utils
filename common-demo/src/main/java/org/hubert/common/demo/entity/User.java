package org.hubert.common.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a user entity.
 * Each user can have multiple roles in a many-to-many relationship.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/29 22:35
 */
@Data
@Entity
@Table(name = "t_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * The unique identifier for the User entity.
     * This field is automatically generated using the IDENTITY strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The unique username for the User entity.
     * It is a mandatory field and must be unique within the database.
     */
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The email address of the User.
     * It is a mandatory field and must be unique within the database.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Stores the password for the User entity.
     * It is a mandatory field.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Represents the roles assigned to a user.
     * This is a Many-to-Many relationship with the {@link Role} entity.
     * The relationship is established through a join table named "user_roles",
     * which contains columns for "user_id" and "role_id" to link users with their roles.
     * <p>
     * The data is lazily fetched, meaning it is only loaded when accessed.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_user_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
