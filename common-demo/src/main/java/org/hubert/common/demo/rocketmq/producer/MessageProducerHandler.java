package org.hubert.common.demo.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.hubert.common.demo.properties.MQProducerOperationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * IMessageProducerHandler is an interface that defines the operations for sending messages
 * to a message broker either synchronously or asynchronously. It also provides
 * methods to retrieve the message producer and its properties and the executor service.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/20 14:55
 */
public interface MessageProducerHandler {
    Logger logger = LoggerFactory.getLogger(MessageProducerHandler.class);

    /**
     * Sends a message synchronously to a specified topic with the given parameters and delay time.
     *
     * @param topic          the topic to which the message is sent
     * @param tags           the tags associated with the message
     * @param keys           the keys used to identify the message
     * @param body           the content of the message in byte array format
     * @param delayTimeLevel the delay level for the message, where a value greater than 0 specifies a delay
     * @return the result of the message send operation, represented by a {@code SendResult} object
     */
    SendResult sendMessageSync(String topic, String tags, String keys, byte[] body, int delayTimeLevel);

    /**
     * Sends a message asynchronously to a specified topic with the given parameters and delay time.
     *
     * @param topic          the topic to which the message is sent
     * @param tags           the tags associated with the message
     * @param keys           the keys used to identify the message
     * @param body           the content of the message in byte array format
     * @param delayTimeLevel the delay level for the message, where a value greater than 0 specifies a delay
     */
    void sendMessageAsync(String topic, String tags, String keys, byte[] body, int delayTimeLevel);

    /**
     * Retrieves the DefaultMQProducer instance used for producing messages.
     *
     * @return the DefaultMQProducer instance
     */
    DefaultMQProducer getProducer();

    /**
     * Retrieves the properties for MQ operations.
     *
     * @return an instance of MQOperationProperties containing the MQ operation settings
     */
    MQProducerOperationProperties getProperties();

    /**
     * Retrieves the ThreadPoolExecutor service used for asynchronously processing tasks.
     *
     * @return the ThreadPoolExecutor service
     */
    ThreadPoolExecutor getExecutorService();

    /**
     * Creates a message with the specified topic, tags, keys, content, and optional delay time.
     *
     * @param topic          the topic to which the message belongs
     * @param tags           the tags used to filter the message
     * @param keys           the keys used to uniquely identify the message
     * @param body           the content of the message in byte array format
     * @param delayTimeLevel the delay level for the message, where a value greater than 0 specifies a delay
     * @return the created message instance
     */
    default Message createMessage(String topic, String tags, String keys, byte[] body, int delayTimeLevel) {
        Message message = new Message(topic, tags, keys, body);
        if (delayTimeLevel > 0) {
            message.setDelayTimeLevel(delayTimeLevel);
        }
        return message;
    }

    /**
     * Sends a message synchronously using the specified message and keys.
     *
     * @param message the message to be sent
     * @param keys    the keys used to route the message
     * @return the result of the message send operation, represented by a {@code SendResult} object
     */
    default SendResult sendMessageInternalSync(Message message, String keys) {
        SendResult result = null;
        try {
            result = getProducer().send(message, (mqs, msg1, arg) -> mqs.get(Math.abs(arg.hashCode()) % mqs.size()), keys);
        } catch (Exception e) {
            logger.error("Error sending message: {} synchronously, keys: {}", message, keys, e);
        }
        return result;
    }

    /**
     * Sends a message asynchronously using the specified message and keys.
     *
     * @param message the message to be sent
     * @param keys    the keys used to route the message
     */
    default void sendMessageInternalAsync(Message message, String keys) {
        ThreadPoolExecutor executorService = getExecutorService();
        if (executorService != null) {
            executorService.submit(() -> sendMessageInternalSync(message, keys));
        } else {
            sendMessageInternalSync(message, keys);
        }
    }
}