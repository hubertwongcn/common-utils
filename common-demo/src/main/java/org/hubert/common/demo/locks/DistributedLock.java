package org.hubert.common.demo.locks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * DistributedLock class for managing distributed locks through Redis.<p>
 * Handles various lock operations using functional interfaces.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/9/14 11:08
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DistributedLock {
    /**
     * DEFAULT_LOCK_TIMEOUT specifies the default duration, in minutes, for
     * which a lock will be held before timing out.<p>
     * This constant is used to handle scenarios where a lock must not be held indefinitely
     * and ensures there is a mechanism for timeout to prevent potential deadlocks.
     */
    private final static int DEFAULT_LOCK_TIMEOUT = 5;
    private final RedisLockRegistry redisLockRegistry;

    /**
     * Executes the given action while holding a lock associated with the specified lock key.
     *
     * @param lockKey   the key to obtain the lock
     * @param lockTimes the time duration to try to acquire the lock
     * @param timeUnit  the time unit of the lock duration
     * @param action    the action to perform if the lock is acquired
     * @return the result of the action if the lock is acquired, otherwise returns null
     */
    private <T> T executeWithLock(Object lockKey, int lockTimes, TimeUnit timeUnit, Supplier<T> action) {
        boolean lockResult = false;
        Lock lock = redisLockRegistry.obtain(lockKey);
        try {
            lockResult = lock.tryLock(lockTimes, timeUnit);
            if (lockResult) {
                return action.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread was interrupted while trying to acquire lock for key: {}", lockKey, e);
        } catch (Exception e) {
            log.error("An unexpected error occurred while holding lock for key: {}", lockKey, e);
        } finally {
            if (lockResult) {
                lock.unlock();
                log.info("Lock released for key: {}", lockKey);
            }
        }
        return null;
    }

    /**
     * Attempts to acquire a lock with the specified key and, if successful, executes the provided action.
     * If the lock cannot be acquired within the default timeout, the method returns without executing the action.
     *
     * @param lockKey the key used to identify the lock
     * @param action  the action to be executed if the lock is successfully acquired
     * @return the result of the action if the lock was acquired, otherwise null
     */
    public <T> T tryLock(Object lockKey, Supplier<T> action) {
        return tryLock(lockKey, DEFAULT_LOCK_TIMEOUT, TimeUnit.SECONDS, action);
    }

    /**
     * Attempts to acquire a lock on the given lock key and perform the specified action.
     *
     * @param lockKey   the key to identify the lock
     * @param lockTimes the number of times to try acquiring the lock
     * @param timeUnit  the time unit for lock expiration
     * @param action    the action to be performed if the lock is acquired
     * @param <T>       the type of the result returned by the action
     * @return the result of the action if the lock is successfully acquired, null otherwise
     */
    public <T> T tryLock(Object lockKey, int lockTimes, TimeUnit timeUnit, Supplier<T> action) {
        return executeWithLock(lockKey, lockTimes, timeUnit, action);
    }

    /**
     * Attempts to acquire a lock associated with the given key and executes the specified action if the lock is acquired.
     *
     * @param lockKey The key associated with the lock to be acquired.
     * @param action  The runnable action to be executed if the lock is successfully acquired.
     */
    public void tryLock(Object lockKey, Runnable action) {
        tryLock(lockKey, DEFAULT_LOCK_TIMEOUT, TimeUnit.SECONDS, action);
    }

    /**
     * Attempts to acquire a lock associated with the given lock key, retrying up to the specified number of
     * times with a delay between attempts based on the provided time unit before running the specified action.
     *
     * @param lockKey   the key associated with the lock to be acquired
     * @param lockTimes the number of attempts to acquire the lock before giving up
     * @param timeUnit  the time unit for the delay between lock attempts
     * @param action    the action to be executed once the lock is acquired
     */
    public void tryLock(Object lockKey, int lockTimes, TimeUnit timeUnit, Runnable action) {
        executeWithLock(lockKey, lockTimes, timeUnit, () -> {
            action.run();
            return null;
        });
    }

    /**
     * Tries to acquire a lock identified by the given lockKey, executes the provided action
     * if the lock is successfully acquired, and then releases the lock.
     *
     * @param <T>     the type of the argument and return value of the action
     * @param lockKey the key identifying the lock to be acquired
     * @param action  the function to be executed if the lock is successfully acquired
     * @param arg     the argument to be passed to the action
     * @return the result of the action if the lock is successfully acquired,
     * or a default value if the lock cannot be acquired
     */
    public <T> T tryLock(Object lockKey, Function<T, T> action, T arg) {
        return tryLock(lockKey, DEFAULT_LOCK_TIMEOUT, TimeUnit.SECONDS, action, arg);
    }

    /**
     * Attempts to acquire a lock based on the provided lock key and executes the specified action with the given argument.
     * If the lock cannot be acquired within the specified time, the action is not executed.
     *
     * @param lockKey   the key to identify the lock
     * @param lockTimes the amount of time to keep trying to acquire the lock
     * @param timeUnit  the time unit for the lockTimes parameter
     * @param action    the action to perform once the lock is acquired
     * @param arg       the argument to pass to the action
     * @param <T>       the type of the argument and the result of the action
     * @return the result of the action if the lock is acquired, otherwise null
     */
    public <T> T tryLock(Object lockKey, int lockTimes, TimeUnit timeUnit, Function<T, T> action, T arg) {
        return executeWithLock(lockKey, lockTimes, timeUnit, () -> action.apply(arg));
    }

    /**
     * Attempts to acquire the lock specified by the given lockKey and executes the provided action
     * with the given argument if the lock is acquired successfully.
     *
     * @param <T>     the type of the argument passed to the action
     * @param lockKey the key used to identify the lock
     * @param action  the action to be executed if the lock is acquired
     * @param arg     the argument to be passed to the action
     * @return true if the lock was acquired and the action was executed successfully; false otherwise
     */
    public <T> boolean tryLock(Object lockKey, Predicate<T> action, T arg) {
        return tryLock(lockKey, DEFAULT_LOCK_TIMEOUT, TimeUnit.SECONDS, action, arg);
    }

    /**
     * Attempts to acquire a lock for a specified period of time and executes an action if the lock is acquired.
     *
     * @param <T>       The type of the argument required by the action.
     * @param lockKey   The key to identify the lock.
     * @param lockTimes The maximum number of times to attempt acquiring the lock.
     * @param timeUnit  The time unit of the lockTimes argument.
     * @param action    The action to be executed if the lock is acquired.
     * @param arg       The argument required by the action.
     * @return true if the action was executed, false otherwise.
     */
    public <T> boolean tryLock(Object lockKey, int lockTimes, TimeUnit timeUnit, Predicate<T> action, T arg) {
        return Boolean.TRUE.equals(executeWithLock(lockKey, lockTimes, timeUnit, () -> action.test(arg)));
    }

    /**
     * Attempts to acquire a lock based on the provided lock key and executes the given action with the specified arguments
     * if the lock is successfully acquired. The method will try to acquire the lock within a default timeout period.
     *
     * @param lockKey the key representing the lock to be acquired
     * @param action  the action to be executed if the lock is successfully acquired, taking two arguments of types T and U
     * @param arg1    the first argument to be passed to the action
     * @param arg2    the second argument to be passed to the action
     */
    public <T, U> void tryLock(Object lockKey, BiConsumer<T, U> action, T arg1, U arg2) {
        tryLock(lockKey, DEFAULT_LOCK_TIMEOUT, TimeUnit.SECONDS, action, arg1, arg2);
    }

    /**
     * Attempts to acquire a lock specified by the given lockKey for a certain number of times
     * and then performs the given action if the lock acquisition is successful.
     *
     * @param lockKey   the key representing the lock to be acquired
     * @param lockTimes the number of times to attempt to acquire the lock
     * @param timeUnit  the unit of time for the lock acquisition attempts
     * @param action    the action to be performed if the lock is acquired
     * @param arg1      the first argument to be passed to the action
     * @param arg2      the second argument to be passed to the action
     */
    public <T, U> void tryLock(Object lockKey, int lockTimes, TimeUnit timeUnit, BiConsumer<T, U> action, T arg1, U arg2) {
        executeWithLock(lockKey, lockTimes, timeUnit, () -> {
            action.accept(arg1, arg2);
            return null;
        });
    }

    /**
     * Attempts to acquire a lock identified by the given lockKey, and if successful,
     * executes the provided action with the specified arguments.
     *
     * @param lockKey the key identifying the lock to be acquired
     * @param action  the action to be executed if the lock is successfully acquired
     * @param arg1    the first argument to be passed to the action
     * @param arg2    the second argument to be passed to the action
     * @param <T>     the type of the first argument to the action
     * @param <U>     the type of the second argument to the action
     * @param <R>     the type of the result produced by the action
     * @return the result produced by the action if the lock is successfully acquired
     */
    public <T, U, R> R tryLock(Object lockKey, BiFunction<T, U, R> action, T arg1, U arg2) {
        return tryLock(lockKey, DEFAULT_LOCK_TIMEOUT, TimeUnit.SECONDS, action, arg1, arg2);
    }

    /**
     * Attempts to acquire a lock on the given key and execute a specified action if the lock is acquired.
     *
     * @param lockKey   the key used to identify the lock
     * @param lockTimes the number of times to try acquiring the lock
     * @param timeUnit  the time unit for the lock wait time
     * @param action    the action to perform once the lock is acquired
     * @param arg1      the first argument to pass to the action
     * @param arg2      the second argument to pass to the action
     * @return the result of the action if the lock is successfully acquired
     */
    public <T, U, R> R tryLock(Object lockKey, int lockTimes, TimeUnit timeUnit, BiFunction<T, U, R> action, T arg1, U arg2) {
        return executeWithLock(lockKey, lockTimes, timeUnit, () -> action.apply(arg1, arg2));
    }

    /**
     * Attempts to acquire a lock on the given lockKey and, if successful, performs the specified action.
     *
     * @param lockKey the key representing the lock to be acquired
     * @param action  the action to be performed if the lock is acquired
     * @param arg     the argument to be passed to the action
     * @return the result of the action performed if the lock was acquired
     */
    public <T> T tryLock(Object lockKey, UnaryOperator<T> action, T arg) {
        return tryLock(lockKey, DEFAULT_LOCK_TIMEOUT, TimeUnit.SECONDS, action, arg);
    }

    /**
     * Attempts to acquire a lock based on the provided lockKey and performs the given action
     * if the lock is successfully acquired within the specified time.
     *
     * @param <T>       the type of the result produced by the action
     * @param lockKey   the object used as the key for locking
     * @param lockTimes the maximum time to acquire the lock
     * @param timeUnit  the unit of time for the lockTimes argument
     * @param action    the action to be performed if the lock is acquired
     * @param arg       the argument to be passed to the action
     * @return the result of type T produced by the action if the lock is acquired, otherwise null
     */
    public <T> T tryLock(Object lockKey, int lockTimes, TimeUnit timeUnit, UnaryOperator<T> action, T arg) {
        return executeWithLock(lockKey, lockTimes, timeUnit, () -> action.apply(arg));
    }

    /**
     * Attempts to acquire a lock on the given lockKey and, if successful,
     * executes the provided action with the specified arguments.
     *
     * @param <T>     the type of the result of the action
     * @param lockKey the key to be used for locking
     * @param action  the action to execute if the lock is successfully acquired
     * @param arg1    the first argument to pass to the action
     * @param arg2    the second argument to pass to the action
     * @return the result of the action if the lock is acquired, or null if the lock could not be acquired
     */
    public <T> T tryLock(Object lockKey, BinaryOperator<T> action, T arg1, T arg2) {
        return tryLock(lockKey, DEFAULT_LOCK_TIMEOUT, TimeUnit.SECONDS, action, arg1, arg2);
    }

    /**
     * Attempts to acquire a lock based on the specified lock key and executes a given action if the lock is obtained.
     *
     * @param lockKey   the key used to identify the lock
     * @param lockTimes the number of times to attempt acquiring the lock
     * @param timeUnit  the time unit for the lock times
     * @param action    the action to be executed if the lock is acquired
     * @param arg1      the first argument to be passed to the action
     * @param arg2      the second argument to be passed to the action
     * @return the result of the action executed under the lock
     */
    public <T> T tryLock(Object lockKey, int lockTimes, TimeUnit timeUnit, BinaryOperator<T> action, T arg1, T arg2) {
        return executeWithLock(lockKey, lockTimes, timeUnit, () -> action.apply(arg1, arg2));
    }

    /**
     * Attempts to acquire a lock associated with the given lockKey, and if successful,
     * executes the provided action with the given argument.
     *
     * @param lockKey The key associated with the lock to be acquired.
     * @param action  The action to be executed if the lock is successfully acquired.
     * @param arg     The argument to be passed to the action.
     * @param <T>     The type of the argument passed to the action.
     */
    public <T> void tryLock(Object lockKey, Consumer<T> action, T arg) {
        tryLock(lockKey, DEFAULT_LOCK_TIMEOUT, TimeUnit.SECONDS, action, arg);
    }

    /**
     * Attempts to acquire a lock on the given key and, if successful, executes the specified action with the provided argument.
     *
     * @param lockKey   the key to be used for locking
     * @param lockTimes the maximum number of times to attempt acquiring the lock
     * @param timeUnit  the time unit for the lock waiting time
     * @param action    the action to be executed if the lock is successfully acquired
     * @param arg       the argument to be passed to the action
     * @param <T>       the type of the argument to be passed to the action
     */
    public <T> void tryLock(Object lockKey, int lockTimes, TimeUnit timeUnit, Consumer<T> action, T arg) {
        executeWithLock(lockKey, lockTimes, timeUnit, () -> {
            action.accept(arg);
            return null;
        });
    }
}