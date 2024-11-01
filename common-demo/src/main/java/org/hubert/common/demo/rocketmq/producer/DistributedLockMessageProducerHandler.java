package org.hubert.common.demo.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.hubert.common.demo.locks.DistributedLock;
import org.hubert.common.demo.properties.MQProducerOperationProperties;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * A message producer handler that implements distributed locking to ensure
 * that messages are sent with proper synchronization.
 * <p>
 * This handler extends the abstract class AbstractMessageProducerHandler and
 * utilizes a distributed lock for message synchronization, making sure that
 * the message production process is thread-safe.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/20 14:58
 */
public class DistributedLockMessageProducerHandler extends AbstractMessageProducerHandler {
    private final String lockKeyPrefix;
    private final DistributedLock distributedLock;

    public DistributedLockMessageProducerHandler(DefaultMQProducer producer,
                                                 MQProducerOperationProperties properties,
                                                 ThreadPoolExecutor executorService,
                                                 DistributedLock distributedLock,
                                                 String lockKeyPrefix) {
        super(producer, properties, executorService);
        this.distributedLock = distributedLock;
        this.lockKeyPrefix = lockKeyPrefix;
    }

    @Override
    public SendResult sendMessageSync(String topic, String tags, String keys, byte[] body, int delayTimeLevel) {
        Message message = createMessage(topic, tags, keys, body, delayTimeLevel);
        String lockKey = lockKeyPrefix + keys;
        return distributedLock.tryLock(lockKey, () -> sendMessageInternalSync(message, keys));
    }

    @Override
    public void sendMessageAsync(String topic, String tags, String keys, byte[] body, int delayTimeLevel) {
        Message message = createMessage(topic, tags, keys, body, delayTimeLevel);
        String lockKey = lockKeyPrefix + keys;
        distributedLock.tryLock(lockKey, () -> {
            sendMessageInternalAsync(message, keys);
            return null;
        });
    }
}