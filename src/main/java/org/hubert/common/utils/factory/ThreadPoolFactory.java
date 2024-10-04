package org.hubert.common.utils.factory;

import org.hubert.common.utils.executor.CustomThreadPoolExecutor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A factory for creating instances of {@link CustomThreadPoolExecutor}.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/14 14:46
 */
public class ThreadPoolFactory {
    /**
     * Creates a new instance of {@link CustomThreadPoolExecutor} with the specified core and maximum pool sizes,
     * keep-alive time, and thread name prefix. The created thread pool uses a fixed capacity queue and a
     * 'discard oldest' rejection policy.
     *
     * @param corePoolSize     the number of core threads in the pool.
     * @param maximumPoolSize  the maximum number of threads in the pool.
     * @param keepAliveTime    the time limit for which threads may remain idle before being terminated.
     * @param threadNamePrefix the prefix for naming the threads in the pool.
     * @return a configured instance of {@link CustomThreadPoolExecutor}.
     */
    public static CustomThreadPoolExecutor createThreadPool(int corePoolSize,
                                                            int maximumPoolSize,
                                                            long keepAliveTime,
                                                            String threadNamePrefix) {
        return createThreadPool(corePoolSize, maximumPoolSize, keepAliveTime,
                threadNamePrefix,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000),
                new ThreadPoolExecutor.DiscardOldestPolicy());
    }


    /**
     * Creates a new instance of {@link CustomThreadPoolExecutor} with the specified configurations.
     *
     * @param corePoolSize     the number of core threads in the pool
     * @param maximumPoolSize  the maximum number of threads allowed in the pool
     * @param keepAliveTime    the maximum time that excess idle threads will wait for new tasks before terminating
     * @param threadNamePrefix the prefix for the names of the threads created by the pool
     * @param timeUnit         the time unit for the {@code keepAliveTime} argument
     * @param workQueue        the queue to use for holding tasks before they are executed
     * @param handler          the handler to use when execution is blocked because the thread bounds and queue capacities are reached
     * @return a configured instance of {@link CustomThreadPoolExecutor}
     */
    public static CustomThreadPoolExecutor createThreadPool(int corePoolSize,
                                                            int maximumPoolSize,
                                                            long keepAliveTime,
                                                            String threadNamePrefix,
                                                            TimeUnit timeUnit,
                                                            BlockingQueue<Runnable> workQueue,
                                                            RejectedExecutionHandler handler) {
        ThreadFactory namedThreadFactory = new NamedThreadFactory(threadNamePrefix);
        return new CustomThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit,
                workQueue, namedThreadFactory, handler);
    }

    /**
     * NamedThreadFactory class to create named threads.
     */
    static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        NamedThreadFactory(String threadNamePrefix) {
            this.namePrefix = threadNamePrefix + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}