package org.hubert.common.utils.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/4 14:49
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rocketmq.consumer.package-bind-op")
public class PackageBindOpPropertiesConsumer extends MQConsumerOperationProperties {
}
