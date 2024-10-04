package org.hubert.common.utils.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.hubert.common.utils.properties.PackageBindOpPropertiesConsumer;
import org.hubert.common.utils.properties.PackageBindOpPropertiesProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/4 14:50
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RocketMQCosnumerConfig {

    private final PackageBindOpPropertiesConsumer packageBindOpPropertiesConsumer;

    @Bean(name = "packageBindOpConsumer")
    public DefaultMQPushConsumer packageBindOpConsumer(PackageBindOpPropertiesProducer packageBindOpPropertiesProducer) {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(packageBindOpPropertiesConsumer.getGroup());
        consumer.setNamesrvAddr(packageBindOpPropertiesConsumer.getNameServer());
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        try {
            consumer.subscribe(packageBindOpPropertiesProducer.getTopic(), "*");
        } catch (Exception e) {
            throw new RuntimeException("Failed to subscribe to topic", e);
        }
        return consumer;
    }
}
