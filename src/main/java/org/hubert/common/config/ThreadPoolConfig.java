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

    @Bean(name = "slinkPackageOpThreadPoolExecutor")
    public CustomThreadPoolExecutor slinkPackageOpProducerThreadPoolExecutor() {
        return ThreadPoolFactory.createThreadPool(1, 1, 1L, "slink-package-op-producer-thread-pool");
    }

    @Bean(name = "slinkPackageOpConsumerThreadPoolExecutor")
    public CustomThreadPoolExecutor consumerThreadPoolExecutor() {
        return ThreadPoolFactory.createThreadPool(10, 20, 1L, "slink-package-op-consumer-thread-pool");
    }
}