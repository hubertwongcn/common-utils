package org.hubert.common.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hubert.common.entity.User;
import org.hubert.common.enums.ResponseEnum;
import org.hubert.common.exceptions.CustomException;
import org.hubert.common.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service implementation for loading user-specific data by username.
 * This class implements the {@link UserDetailsService} interface,
 * and is annotated with {@link Service} to indicate that it is a Spring service component.
 * The required dependencies are automatically injected using the {@link RequiredArgsConstructor} annotation.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/29 23:07
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * The userRepository is an instance of {@link UserRepository}, used to interact with the
     * underlying database for performing operations related to the User entity.
     * It provides methods to find, check existence, and manipulate user records
     * in the database.
     */
    private final UserRepository userRepository;

    /**
     * Loads the user-specific data by the given username.
     *
     * @param username the unique username to search for
     * @return the UserDetails object containing user-specific data
     * @throws UsernameNotFoundException if no user is found with the specified username
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ResponseEnum.USER_NOT_FOUND));
        return UserDetailsImpl.build(user);
    }
}
