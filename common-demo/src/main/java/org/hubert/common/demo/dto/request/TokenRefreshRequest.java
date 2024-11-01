package org.hubert.common.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * A request object used for refreshing JWT tokens.
 * This object encapsulates the refresh token needed to generate a new JWT.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/30 09:43
 */
@Data
public class TokenRefreshRequest {
    /**
     * The refresh token used for requesting a new JWT token.
     * This token is typically issued alongside the original JWT token
     * and allows clients to obtain a new JWT without re-authenticating.
     */
    @NotBlank(message = "RefreshToken cannot be blank")
    private String refreshToken;
}
