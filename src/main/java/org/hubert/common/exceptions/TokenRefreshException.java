package org.hubert.common.exceptions;

import java.io.Serial;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/30 09:18
 */
public class TokenRefreshException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public TokenRefreshException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
