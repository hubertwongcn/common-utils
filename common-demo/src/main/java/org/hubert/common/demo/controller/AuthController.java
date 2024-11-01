package org.hubert.common.demo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.hubert.common.demo.dto.request.LoginRequest;
import org.hubert.common.demo.dto.request.SignupRequest;
import org.hubert.common.demo.dto.request.TokenRefreshRequest;
import org.hubert.common.demo.dto.response.JwtResponse;
import org.hubert.common.demo.dto.response.TokenRefreshResponse;
import org.hubert.common.demo.entity.RefreshToken;
import org.hubert.common.demo.entity.Role;
import org.hubert.common.demo.entity.User;
import org.hubert.common.demo.enums.ResponseEnum;
import org.hubert.common.demo.enums.RoleEnum;
import org.hubert.common.demo.exceptions.CustomException;
import org.hubert.common.demo.exceptions.TokenRefreshException;
import org.hubert.common.demo.repository.RoleRepository;
import org.hubert.common.demo.repository.UserRepository;
import org.hubert.common.demo.result.Result;
import org.hubert.common.demo.security.JwtTokenProvider;
import org.hubert.common.demo.service.JwtTokenService;
import org.hubert.common.demo.service.RefreshTokenService;
import org.hubert.common.demo.service.impl.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * AuthController is responsible for handling authentication related HTTP requests, including user sign-in,
 * sign-up, and token refresh operations.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/30 09:36
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    /**
     * An instance of {@link AuthenticationManager} used to handle authentication processes.
     * It is responsible for authenticating user credentials and providing methods to verify the authentication state.
     * This field is marked as final, indicating it is immutable after initialization.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Repository interface for user-related database operations.
     * Provides methods for retrieving and checking for the existence of users by username and email.
     */
    private final UserRepository userRepository;

    /**
     * Responsible for data access operations related to roles within the authentication process.
     * Provides methods to interact with the {@link Role} entities stored in the database.
     * Primarily used in the authentication controller to manage user roles during the login and registration processes.
     */
    private final RoleRepository roleRepository;

    /**
     * A PasswordEncoder instance used for encoding and decoding user passwords.
     * It is leveraged in user authentication and registration processes within the AuthController class.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Service responsible for handling operations related to refresh tokens.
     * It includes functionalities for creating, finding, and verifying the expiration of refresh tokens.
     */
    private final RefreshTokenService refreshTokenService;

    /**
     * Provides JWT (JSON Web Token) creation and validation functionalities.
     * Used for handling authentication tokens within the application.
     */
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Service responsible for handling JSON Web Tokens (JWT).
     * Provides methods for generating, validating, and retrieving JWT tokens.
     * Used within the {@link AuthController} to manage user authentication and authorization.
     */
    private final JwtTokenService jwtTokenService;

    /**
     * Authenticates a user based on the provided login request, generates a JWT token,
     * updates the current JWT, creates a refresh token, and returns a response with the JWT and refresh tokens.
     *
     * @param loginRequest the login request containing the username and password for authentication
     * @return a {@link JwtResponse} containing the generated JWT, refresh token, and expiration information
     */
    @PostMapping("/signin")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserDetailsImpl userDetails = (UserDetailsImpl) authenticate.getPrincipal();
        String jwt = jwtTokenProvider.generateToken(authenticate);
        jwtTokenService.updateCurrentJwt(userDetails.getUsername(), jwt, jwtTokenProvider.getJwtExpirationInMs());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return new JwtResponse(jwt, refreshToken.getToken(),
                refreshToken.getExpireDate().toEpochMilli(),
                (Instant.now().toEpochMilli() + jwtTokenProvider.getJwtExpirationInMs() - 500));
    }

    /**
     * Registers a new user based on the provided signup request. This method checks for existing
     * users with the same username or email, assigns appropriate roles, and saves the new user
     * to the database.
     *
     * @param signupRequest the signup request containing username, email, password, and optionally, roles.
     * @return a Result object indicating success or specific error messages if the username or email already exists.
     */
    @PostMapping("/signup")
    public Result<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return Result.error(ResponseEnum.USER_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return Result.error(ResponseEnum.EMIAL_ALREADY_EXISTS);
        }
        User user = new User(signupRequest.getUsername(), signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()));
        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (CollectionUtils.isEmpty(strRoles)) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new CustomException(ResponseEnum.ROLE_NOT_FOUND));
            roles.add(userRole);
        } else {
            strRoles.stream()
                    .map(role -> RoleEnum.valueOf(role.toUpperCase()))
                    .map(roleEnum -> roleRepository.findByName(roleEnum)
                            .orElseThrow(() -> new CustomException(ResponseEnum.ROLE_NOT_FOUND)))
                    .forEach(roles::add);
        }
        user.setRoles(roles);
        userRepository.save(user);
        return Result.success();
    }

    /**
     * Refreshes the JWT token using a valid refresh token provided in the request.
     * If the refresh token is valid and not expired, a new JWT token is generated
     * and the existing refresh token is returned.
     *
     * @param request the token refresh request containing the refresh token.
     * @return a {@link TokenRefreshResponse} containing the new JWT and the provided refresh token.
     * @throws TokenRefreshException if the provided refresh token is not found or has expired.
     */
    @PostMapping("/refreshToken")
    public TokenRefreshResponse refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshToken -> {
                    long tokenExpiresAt = refreshToken.getExpireDate().toEpochMilli();
                    refreshTokenService.verifyExpiration(refreshToken);
                    if (refreshToken.getUser() == null) {
                        throw new TokenRefreshException(requestRefreshToken, "Refresh token user not found");
                    }
                    String token = jwtTokenProvider.generateTokenFromUsername(refreshToken.getUser().getUsername());
                    jwtTokenService.updateCurrentJwt(refreshToken.getUser().getUsername(), token, jwtTokenProvider.getJwtExpirationInMs());
                    return new TokenRefreshResponse(token, requestRefreshToken, tokenExpiresAt,
                            (Instant.now().toEpochMilli() + jwtTokenProvider.getJwtExpirationInMs() - 500));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not found"));
    }
}
