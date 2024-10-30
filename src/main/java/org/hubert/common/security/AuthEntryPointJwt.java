package org.hubert.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hubert.common.result.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * AuthEntryPointJwt is the entry point for handling authentication exceptions
 * in a Spring Security context. This component is specifically designed to return
 * an HTTP 401 Unauthorized response when an authentication failure occurs.
 * <p>
 * The {@link #commence(HttpServletRequest, HttpServletResponse, AuthenticationException)}
 * method is overridden to customize the response when an authentication exception is thrown.
 * The response includes a JSON-formatted message indicating that the user is unauthorized.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/29 23:17
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    /**
     * Handles an authentication exception by setting the response status to 401 (Unauthorized)
     * and returning a JSON-formatted error message with the exception details.
     *
     * @param request       the HTTP request that resulted in an AuthenticationException.
     * @param response      the HTTP response to be sent to the client.
     * @param authException the exception that was thrown due to authentication failure.
     * @throws IOException      if an input or output error is detected when the handler is processing the request.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Result<Object> result = Result.error(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: " + authException.getMessage());
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
