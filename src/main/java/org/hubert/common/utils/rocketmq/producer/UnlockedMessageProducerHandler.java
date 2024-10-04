package org.hubert.common.utils.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.hubert.common.utils.properties.MQProducerOperationProperties;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * UnlockedMessageProducerHandler is a concrete implementation of {@link AbstractMessageProducerHandler}
 * that provides mechanisms to send messages either synchronously or asynchronously.
 * It leverages the provided {@link DefaultMQProducer}, MQ operation properties, and a thread pool executor service.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/20 15:13
 */
public class UnlockedMessageProducerHandler extends AbstractMessageProducerHandler {

    public UnlockedMessageProducerHandler(DefaultMQProducer producer, MQProducerOperationProperties properties,
                                          ThreadPoolExecutor executorService) {
        super(producer, properties, executorService);
    }

    @Override
    public SendResult sendMessageSync(String topic, String tags, String keys, byte[] body, int delayTimeLevel) {
        Message message = createMessage(topic, tags, keys, body, delayTimeLevel);
        return sendMessageInternalSync(message, keys);
    }

    @Override
    public void sendMessageAsync(String topic, String tags, String keys, byte[] body, int delayTimeLevel) {
        Message message = createMessage(topic, tags, keys, body, delayTimeLevel);
        sendMessageInternalAsync(message, keys);
    }
}