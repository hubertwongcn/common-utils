package org.hubert.common.config;

import org.hubert.common.executor.CustomThreadPoolExecutor;
import org.hubert.common.factory.ThreadPoolFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/20 15:29
 */
@Configuration
public class ThreadPoolConfig {

    @Bean(name = "packageOpThreadPoolExecutor")
    public CustomThreadPoolExecutor packageOpProducerThreadPoolExecutor() {
        return ThreadPoolFactory.createThreadPool(1, 1, 1L, "package-op-producer-thread-pool");
    }

    @Bean(name = "packageOpConsumerThreadPoolExecutor")
    public CustomThreadPoolExecutor consumerThreadPoolExecutor() {
        return ThreadPoolFactory.createThreadPool(10, 20, 1L, "package-op-consumer-thread-pool");
    }
}