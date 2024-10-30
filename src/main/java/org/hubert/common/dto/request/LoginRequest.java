package org.hubert.common.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Represents a request for user login containing the username and password.
 * <p>
 * This class is used as a data transfer object (DTO) for login operations,
 * specifically in the context of authenticating a user.
 * It holds the necessary information required to verify the user's credentials.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/30 09:40
 */
@Data
public class LoginRequest {
    /**
     * The username of the user attempting to log in.
     * This field holds the unique identifier or name that the user uses as part of their login credentials.
     * It is required for authentication purposes.
     */
    @NotBlank(message = "Username cannot be blank")
    private String username;
    /**
     * The password of the user attempting to log in.
     * This field contains the secret string used in combination with the username
     * to authenticate the user.
     * It is crucial for ensuring that the login process remains secure.
     */
    @NotBlank(message = "Password cannot be blank")
    private String password;
}
