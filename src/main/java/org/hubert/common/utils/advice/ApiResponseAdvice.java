package org.hubert.common.utils.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hubert.common.utils.result.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/2 13:32
 */
@ControllerAdvice/*(basePackages = "org.hubert.common.utils.controller")*/
public class ApiResponseAdvice<T> implements ResponseBodyAdvice<T> {

    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public T beforeBodyWrite(T body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
                             @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType,
                             @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {

        if (body instanceof Result<?>) {
            return body;
        }
        if ((body instanceof String)) {
            return toJson(body);
        }
        return wrapAsResult(body);
    }

    @SuppressWarnings("unchecked")
    private T wrapAsResult(Object body) {
        return (T) Result.success(body);
    }

    @SuppressWarnings("unchecked")
    private T toJson(Object body) {
        try {
            return (T) new ObjectMapper().writeValueAsString(Result.success(body));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to forward json request", e);
        }
    }
}
