package org.hubert.common.rocketmq.producer;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.hubert.common.locks.DistributedLock;
import org.hubert.common.properties.PackageBindOpPropertiesProducer;
import org.hubert.common.properties.RedisKeyProperties;
import org.hubert.common.rocketmq.msg.DeviceOpMsg;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * This is an example implementation of a producer handler for package binding operations.<p>
 * Handles asynchronous message production for package binding operations using RocketMQ.<p>
 * This handler ensures that messages are sent sequentially using a single-threaded executor.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/14 10:27
 */
@Slf4j
@Component
public class PackageBindOpProducerHandler extends DistributedLockMessageProducerHandler {

    private final PackageBindOpPropertiesProducer properties;

    public PackageBindOpProducerHandler(
            @Qualifier("packageBindOpProducer") DefaultMQProducer packageBindOpProducer,
            PackageBindOpPropertiesProducer properties,
            @Qualifier("packageOpThreadPoolExecutor") ThreadPoolExecutor executorService,
            DistributedLock distributedLock,
            RedisKeyProperties redisKeyProperties) {
        super(packageBindOpProducer, properties, executorService, distributedLock, redisKeyProperties.getPackageChangeMsg());
        this.properties = properties;
    }

    /**
     * Sends a synchronous message for user device operations.
     *
     * @param deviceOpMsg the user device operation message that needs to be sent
     * @return the result of the send operation
     */
    public SendResult sendSync(DeviceOpMsg deviceOpMsg) {
        byte[] body = JSON.toJSONString(deviceOpMsg).getBytes();
        return sendMessageSync(properties.getTopic(), properties.getTag(),
                deviceOpMsg.getUserCode() + deviceOpMsg.getPackageId(), body, 0);
    }

    /**
     * Sends an asynchronous message for user device operations.
     *
     * @param deviceOpMsg the user device operation message that needs to be sent
     */
    public void sendAsync(DeviceOpMsg deviceOpMsg) {
        byte[] body = JSON.toJSONString(deviceOpMsg).getBytes();
        sendMessageAsync(properties.getTopic(), properties.getTag(),
                deviceOpMsg.getUserCode() + deviceOpMsg.getPackageId(), body, 0);
    }
}