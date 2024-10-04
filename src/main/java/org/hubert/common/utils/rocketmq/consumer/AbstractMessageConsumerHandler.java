package org.hubert.common.utils.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/4 14:06
 */
public abstract class AbstractMessageConsumerHandler implements MessageConsumerHandler {
    private static final Logger log = LoggerFactory.getLogger(AbstractMessageConsumerHandler.class);
    /**
     * The default maximum number of retry attempts for processing a message.
     * This value is used to limit the number of retries in case of failures during message consumption.
     */
    private static final int DEFAULT_MAX_RETRY_TIMES = 3;
    /**
     * The default maximum number of concurrent tasks that can be executed simultaneously.
     * This value is used to control the concurrency level in the message consumption process.
     */
    private static final int DEFAULT_MAX_CONCURRENT_TASKS = 10;

    protected final DefaultMQPushConsumer consumer;

    protected final ExecutorService executorService;

    private final Semaphore semaphore;

    protected AbstractMessageConsumerHandler(DefaultMQPushConsumer consumer, ExecutorService executorService) {
        this.consumer = consumer;
        this.executorService = executorService;
        this.semaphore = new Semaphore(DEFAULT_MAX_CONCURRENT_TASKS);
        startConsumer();
    }

    private void startConsumer() {
        consumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context) -> {
            for (MessageExt msg : msgs) {
                executorService.execute(() -> {
                    boolean acquired = false;
                    try {
                        semaphore.acquire();
                        acquired = true;
                        String messageBody = new String(msg.getBody(), StandardCharsets.UTF_8);
                        log.info("Received message: " + messageBody);

                        if (filterMessage(messageBody)) {
                            log.info("Filtered out message: " + messageBody);
                            return;
                        }

                        // 幂等性检查
                        if (isDuplicate(messageBody)) {
                            log.warn("Duplicate message: " + messageBody);
                            return;
                        }

                        int retryTimes = msg.getReconsumeTimes();
                        boolean success = processMessage(messageBody);
                        if (!success) {
                            log.warn("Failed to process message: " + messageBody);
                            if (retryTimes < DEFAULT_MAX_RETRY_TIMES) {
                                log.info("Retrying message: " + messageBody + " (Retry " + (retryTimes + 1) + ")");
                                context.setDelayLevelWhenNextConsume(retryTimes + 1);
                            } else {
                                log.error("Exceeded max retry attempts for message: " + messageBody);
                            }
                        } else {
                            log.info("Successfully processed message: " + messageBody);
                        }
                    } catch (Exception e) {
                        log.error("Exception while processing message: " + msg, e);
                    } finally {
                        if (acquired) {
                            semaphore.release();
                        }
                    }
                });
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        try {
            consumer.start();
        } catch (Exception e) {
            throw new RuntimeException("Failed to start RocketMQ consumer", e);
        }
    }

    /**
     * Processes the given message and performs necessary actions depending on the message content, implemented by subclasses.
     *
     * @param message the message to be processed
     * @return true if the message was successfully processed, false otherwise
     */
    @Override
    public abstract boolean processMessage(String message);

    /**
     * Filters the specified message based on custom criteria defined by subclasses.
     *
     * @param message the message to be filtered
     * @return true if the message should be filtered out, false otherwise
     */
    protected boolean filterMessage(String message) {
        return false;
    }

    /**
     * Checks if the provided message is a duplicate.
     * This method should be overridden by subclasses to provide specific duplicate detection logic.
     *
     * @param message the message to be checked for duplication
     * @return true if the message is a duplicate, false otherwise
     */
    protected boolean isDuplicate(String message) {
        return false;
    }
}
