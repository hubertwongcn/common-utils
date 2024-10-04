package org.hubert.common.utils.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * properties for packageBindOp rocketmq producer
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/14 09:58
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "rocketmq.producer.package-bind-op")
public class PackageBindOpPropertiesProducer extends MQProducerOperationProperties {
    private Boolean retryAnotherBrokerWhenNotStoreOK;
    private Integer retryTimesWhenSendFailed;
    private Integer sendMsgTimeout;
}