package org.hubert.common.demo.rocketmq.producer;

import jakarta.annotation.PreDestroy;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.hubert.common.demo.properties.MQProducerOperationProperties;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * AbstractMessageProducerHandler is an abstract class that provides base functionality
 * for handling message production using a {@link DefaultMQProducer}, MQ operation properties,
 * and a thread pool executor service.
 * <p>
 * This class implements the {@link MessageProducerHandler} interface and
 * provides common implementations for retrieving the producer, properties,
 * and executor service. Additionally, it handles the graceful shutdown
 * of the producer and executor service upon destruction.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/20 14:55
 */
public abstract class AbstractMessageProducerHandler implements MessageProducerHandler {
    protected final DefaultMQProducer producer;
    protected final MQProducerOperationProperties properties;
    protected final ThreadPoolExecutor executorService;

    public AbstractMessageProducerHandler(DefaultMQProducer producer, MQProducerOperationProperties properties,
                                          ThreadPoolExecutor executorService) {
        this.producer = producer;
        this.properties = properties;
        this.executorService = executorService;
    }

    @Override
    public DefaultMQProducer getProducer() {
        return this.producer;
    }

    @Override
    public MQProducerOperationProperties getProperties() {
        return this.properties;
    }

    @Override
    public ThreadPoolExecutor getExecutorService() {
        return this.executorService;
    }

    @PreDestroy
    public void shutDownProducer() {
        if (producer != null) {
            producer.shutdown();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
