package org.hubert.common.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.hubert.common.demo.properties.PackageBindOpPropertiesProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User device package binding/unbinding message sender configuration
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/14 09:10
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class PackageBindOpProducerConfig {

    private final PackageBindOpPropertiesProducer properties;

    @Bean(name = "packageBindOpProducer")
    public DefaultMQProducer packageBindOpProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(properties.getGroup());
        producer.setNamesrvAddr(properties.getNameServer());
        producer.setRetryTimesWhenSendFailed(properties.getRetryTimesWhenSendFailed());
        producer.setSendMsgTimeout(properties.getSendMsgTimeout());
        producer.setRetryAnotherBrokerWhenNotStoreOK(properties.getRetryAnotherBrokerWhenNotStoreOK());
        producer.setInstanceName("packageBindOpProducer");
        try {
            producer.start();
            log.info("packageBindOpProducer start success");
        } catch (MQClientException e) {
            log.error("packageBindOpProducer started error", e);
        }
        return producer;
    }
}