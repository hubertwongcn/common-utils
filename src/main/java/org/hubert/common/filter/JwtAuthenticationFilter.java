package org.hubert.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hubert.common.security.JwtTokenProvider;
import org.hubert.common.service.JwtTokenService;
import org.hubert.common.service.impl.UserDetailsServiceImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter is a custom filter that handles JWT authentication and authorization.
 * This filter extends OncePerRequestFilter to ensure each request is filtered only once within a single request cycle.
 * The filter performs the following tasks:
 * <ul>
 * <li>Parses the JWT token from the "Authorization" header.
 * <li>Validates the parsed JWT token.
 * <li>Extracts the username from the validated token.
 * <li>Loads the user details using the extracted username.
 * <li>Sets the authentication in the security context if the token is valid and the user is authenticated.
 * </ul>
 * <p>
 * Dependencies:
 * <ul>
 * <li>JwtTokenProvider: Provides methods to work with JWT tokens (generate, validate, extract information).
 * <li>UserDetailsServiceImpl: Loads user-specific data by username.
 * <li>JwtTokenService: Manages current JWT tokens in a Redis cache.
 * </ul>
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/29 23:19
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /**
     * Provides methods to work with JWT tokens such as generation, validation, and extraction of information.
     * Used within the JwtAuthenticationFilter to handle the JWT parsing and validation process.
     * The JwtTokenProvider is responsible for verifying the token integrity and extracting the relevant user information.
     */
    private final JwtTokenProvider tokenProvider;
    /**
     * An instance of {@link UserDetailsServiceImpl} which handles
     * the retrieval and management of user details for the application.
     * This service is responsible for interactions related to user
     * information such as loading user-specific data.
     */
    private final UserDetailsServiceImpl userDetailsService;
    /**
     * This variable represents an instance of the JwtTokenService,
     * which is responsible for handling operations related to JSON Web Tokens (JWT).
     * The operations include generating, validating, and parsing JWT tokens.
     */
    private final JwtTokenService jwtTokenService;

    /**
     * Filters incoming requests and sets up the security context if a valid JWT is found.
     *
     * @param request     the HttpServletRequest object
     * @param response    the HttpServletResponse object
     * @param filterChain the FilterChain object that enables the request to proceed
     * @throws ServletException if the request could not be handled
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (!StringUtils.hasText(jwt) || !tokenProvider.validateJwtToken(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }
            String username = tokenProvider.getUsernameFromToken(jwt);
            if (!jwt.equals(jwtTokenService.getCurrentJwt(username))) {
                filterChain.doFilter(request, response);
                return;
            }
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error("set authentication error", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of an HTTP request.
     *
     * @param request the HttpServletRequest containing the Authorization header
     * @return the JWT token if present and properly formatted, otherwise null
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}
