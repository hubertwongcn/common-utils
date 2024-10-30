package org.hubert.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A response object used for providing the results of a token refresh operation.
 * This object encapsulates the new JWT token, the refresh token, and the expiration time of the token.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/30 09:43
 */
@Data
@AllArgsConstructor
public class TokenRefreshResponse {
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
}
