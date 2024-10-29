package org.hubert.common.handler;

import org.hubert.common.enums.ResponseEnum;
import org.hubert.common.exceptions.CustomException;
import org.hubert.common.result.Result;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/2 13:44
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public Result<Void> handleCustomException(CustomException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result<Void> handleException(Exception e) {
        return Result.error(ResponseEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }
}
