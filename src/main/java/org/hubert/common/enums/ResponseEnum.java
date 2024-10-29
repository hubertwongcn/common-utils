package org.hubert.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/2 13:27
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {
    SUCCESS(200, "Success"),
    BAD_REQUEST(400, "Bad Request"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    CUSTOM_ERROR(1001, "Custom Error"),
    ;
    private final Integer code;
    private final String message;
}
