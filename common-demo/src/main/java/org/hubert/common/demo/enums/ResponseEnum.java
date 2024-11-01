package org.hubert.common.demo.enums;

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
    USER_ALREADY_EXISTS(1002, "User Already Exists"),
    EMIAL_ALREADY_EXISTS(1003, "Email already exists"),
    USER_NOT_FOUND(1004, "User not found"),
    ROLE_NOT_FOUND(1005, "Role not found"),
    ;
    private final Integer code;
    private final String message;
}
