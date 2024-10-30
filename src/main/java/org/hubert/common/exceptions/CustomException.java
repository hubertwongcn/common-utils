package org.hubert.common.exceptions;

import lombok.Getter;
import org.hubert.common.enums.ResponseEnum;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/2 13:42
 */
@Getter
public class CustomException extends RuntimeException {
    private final Integer code;

    public CustomException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(ResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
    }
}
