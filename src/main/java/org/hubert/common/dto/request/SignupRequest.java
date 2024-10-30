package org.hubert.common.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * Represents a request for user signup, capturing the necessary details
 * such as username, password, email, and roles.
 * <p>
 * This class is used to gather the information needed to register a new user.
 * The roles field, if provided, indicates the roles to be assigned to the user,
 * otherwise, a default role is assigned.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/30 09:41
 */
@Data
public class SignupRequest {
    /**
     * The username of the user attempting to sign up.
     * This field holds the unique identifier or name that the user will use as part of their login credentials.
     * It is required for registration purposes.
     */
    @NotBlank(message = "Username cannot be blank")
    private String username;

    /**
     * The email of the user attempting to sign up.
     * This field contains the email address that will be associated with the new user account.
     * It is required for registration purposes.
     */
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    /**
     * The password of the user attempting to sign up.
     * This field contains the secret string used in combination with the username
     * to authenticate the user. It must conform to the specified complexity requirements.
     */
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 10, max = 16, message = "Password must be between 10 and 16 characters")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[-.&]).{10,16}$",
            message = "Password must be 10-16 characters long and include at least one digit, one uppercase letter, one lowercase letter, and one special character (-.&)"
    )
    private String password;

    /**
     * The roles of the user attempting to sign up.
     * This field contains a set of roles that will be assigned to the new user account.
     */
    private Set<String> roles;
}
