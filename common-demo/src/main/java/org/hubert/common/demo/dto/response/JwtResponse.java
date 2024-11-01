package org.hubert.common.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a response that includes a JWT token, refresh token, expiration information,
 * and user details such as username, email, and roles.
 * <p>
 * This response is typically used in authentication mechanisms where a JWT and refresh token
 * are issued to the user upon successful login or token refresh.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/30 09:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    /**
     * The JSON Web Token (JWT) issued to the user upon successful authentication.
     * It is used to identify and grant access to the user in subsequent requests.
     */
    private String token;
    /**
     * The refresh token issued alongside the JWT, used to obtain a new JWT
     * when the current one expires without requiring the user to log in again.
     */
    private String refreshToken;
    /**
     * The expiration time of the JSON Web Token (JWT) issued to the user in milliseconds since epoch.
     * This indicates the point in time at which the token is no longer valid
     * and cannot be used for authentication.
     */
    private Long tokenExpiresAt;

    /**
     * The expiration time of the refresh token issued to the user in milliseconds since epoch.
     * This indicates the point in time at which the refresh token is no longer valid.
     */
    private Long refreshTokenExpiresAt;
    /**
     * The username of the authenticated user.
     * This property holds the unique identifier used to distinguish the user within the system.
     */
    private String username;
    /**
     * The email address of the authenticated user.
     * This property holds the email associated with the user's account
     * and is typically used for communication and identification purposes.
     */
    private String email;
    /**
     * The roles associated with the authenticated user.
     * This property holds a list of roles or permissions that the user has within the system,
     * typically used for authorization and access control purposes.
     */
    private List<String> roles;

    public JwtResponse(String token, String refreshToken, Long tokenExpiresAt, Long refreshTokenExpiresAt) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenExpiresAt = tokenExpiresAt;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }
}
