package org.hubert.common.demo.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties class that holds the prefixes used for various Redis keys.
 * These prefixes are loaded from the application properties file,
 * with default values provided.
 * This helps in organizing and distinguishing different sets of keys in a Redis-based system.
 * <p>
 * Example application.properties:
 * <p>
 * distributed.lock.prefix.ehomeOrder=ehome:order:
 * distributed.lock.prefix.packageChangeMsg=${distributed.lock.prefix.ehomeOrder}package:msg:lock
 * <p>
 * If distributed.lock.prefix.ehomeOrder and distributed.lock.prefix.packageChangeMsg
 * are not configured in application.properties, the provided default values will be used.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/4 18:40
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = "redis.key.prefix")
public class RedisKeyProperties {

    /**
     * It is recommended to use the project name<p>
     * Common lock prefix, all redis keys should start with this prefix
     */
    @Getter
    private String commonPrefix = "common:utils:";

    /**
     * Represents the key prefix for a message indicating a change in a package.
     * This string is used as part of a larger key in Redis to store or retrieve
     * package change-related messages.
     */
    private String packageChangeMsg = "package:change:msg:key:";

    /**
     * Represents the key prefix for storing refresh tokens in Redis.
     * This string is used as part of a larger key in Redis to store or retrieve
     * refresh token-related data.
     */
    private String refreshTokenKey = "refresh:token:key:";

    /**
     * Represents the key prefix for storing user JWTs in Redis.
     * This string is used as part of a larger key in Redis to store or retrieve
     * user JWT-related data.
     */
    private String userJwtKey = "user:jwt:key:";

    public String getPackageChangeMsg() {
        return commonPrefix + packageChangeMsg;
    }

    public String getRefreshTokenKey() {
        return commonPrefix + refreshTokenKey;
    }

    public String getUserJwtKey() {
        return commonPrefix + userJwtKey;
    }
}
