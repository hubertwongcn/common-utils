package org.hubert.common.rocketmq.consumer;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.hubert.common.rocketmq.msg.UserDeviceOpMsg;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/4 15:00
 */
@Slf4j
@Component
public class UserDeviceOpConsumerHandler extends AbstractMessageConsumerHandler {
    public UserDeviceOpConsumerHandler(@Qualifier("packageBindOpConsumer") DefaultMQPushConsumer consumer,
                                       @Qualifier("packageOpConsumerThreadPoolExecutor") ExecutorService executorService) {
        super(consumer, executorService);
    }

    @Override
    public boolean processMessage(String message) {
        try {
            UserDeviceOpMsg userDeviceOpMsg = JSON.parseObject(message, UserDeviceOpMsg.class);
            // business logic processing
            log.info("Consumed message: " + userDeviceOpMsg);
            return true;
        } catch (Exception e) {
            log.error("Failed to process message: " + message, e);
            // Processing failed, need to try again
            return false;
        }
    }

    @Override
    protected boolean filterMessage(String message) {
        // Add custom filtering logic, such as whether the message content conforms to a certain format
        return super.filterMessage(message);
    }

    @Override
    protected boolean isDuplicate(String message) {
        // Add custom idempotence detection logic, such as checking whether a record with the same ID already exists in the database
        return super.isDuplicate(message);
    }
}
