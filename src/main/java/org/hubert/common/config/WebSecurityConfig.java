package org.hubert.common.config;

import lombok.RequiredArgsConstructor;
import org.hubert.common.filter.JwtAuthenticationFilter;
import org.hubert.common.security.AuthEntryPointJwt;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security that sets up security settings and filters for the web application.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/30 10:19
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {
    /**
     * Custom authentication entry point for handling unauthorized access in JWT-based authentication.
     * This class is used to send a JSON response with an error message and status code 401 (Unauthorized)
     * when an authentication exception occurs.
     */
    private final AuthEntryPointJwt authEntryPointJwt;

    /**
     * JwtAuthenticationFilter is a custom filter that handles JWT authentication for incoming HTTP requests.
     * It extracts the JWT token from the request header, validates it, and sets the authentication in the
     * SecurityContext if the token is valid and matches the stored token.
     */
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Configures a PasswordEncoder bean to be used throughout the application for encoding passwords.
     *
     * @return a BCryptPasswordEncoder instance for password encoding.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures and returns an AuthenticationManager bean used in the application for managing
     * authentication processes.
     *
     * @param authenticationConfiguration the configuration object for authentication settings.
     * @return an AuthenticationManager instance based on the given authentication configuration.
     * @throws Exception if an error occurs while retrieving the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures and returns the Spring Security filter chain.
     * The security configuration is designed to disable CORS and CSRF, handle exceptions using
     * a custom authentication entry point, manage sessions statelessly, and authorize HTTP requests
     * such that requests to "/api/auth/**" are accessible by all while other requests require authentication.
     * Additionally, a JWT authentication filter is added before the UsernamePasswordAuthenticationFilter.
     *
     * @param http the HttpSecurity to modify based on the specified configurations.
     * @return the configured SecurityFilterChain instance.
     * @throws Exception if there is an error during the configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authEntryPointJwt))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/api/auth/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
