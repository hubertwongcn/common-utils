package org.hubert.common.service.impl;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hubert.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the UserDetails interface, providing user-specific data for authentication
 * and authorization purposes.
 * <p>
 * This class implements methods from the UserDetails interface to support Spring Security's
 * authentication process.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/29 22:59
 */
@Data
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The unique identifier for the UserDetailsImpl instance.
     * This ID corresponds to the user's primary key in the database.
     */
    private Long id;
    /**
     * The unique username associated with the user.
     * This field is crucial for identification and authentication within the system.
     */
    private String username;
    /**
     * The email address of the user.
     * It is a mandatory field and must be unique within the database.
     */
    private String email;
    /**
     * Stores the hashed password for the user.
     * This field is crucial for authenticating users and securing their accounts.
     */
    private String password;
    /**
     * A collection of authorities granted to the user.
     * This collection defines the roles and permissions assigned to the user
     * which are used during the authorization process.
     */
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // todo Improve the verification
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
