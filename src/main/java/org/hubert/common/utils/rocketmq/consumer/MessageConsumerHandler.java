package org.hubert.common.utils.rocketmq.consumer;

/**
 * An interface that defines a handler for consuming messages.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/4 14:02
 */
public interface MessageConsumerHandler {

    /**
     * Processes the given message and performs necessary actions depending on the message content.
     *
     * @param message the message to be processed
     * @return true if the message was successfully processed, false otherwise
     */
    boolean processMessage(String message);
}
