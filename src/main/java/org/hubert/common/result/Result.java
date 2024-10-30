package org.hubert.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hubert.common.enums.ResponseEnum;

/**
 * Generic Result class for wrapping API responses.
 *
 * @param <T> The type of data contained in the response.
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/30 23:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success() {
        return new Result<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage(), null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResponseEnum.SUCCESS.getCode(), message, data);
    }

    public static <T> Result<T> error(ResponseEnum responseEnum) {
        return new Result<>(responseEnum.getCode(), responseEnum.getMessage(), null);
    }

    public static <T> Result<T> error(int status, String message) {
        return new Result<>(status, message, null);
    }

    public static <T> Result<T> error(int status, String message, T data) {
        return new Result<>(status, message, data);
    }
}