package net.alaarc.vm;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Multiple-writer multiple-reader read-write mutex.
 *
 * What guarantees does this mutex provide?
 *
 * @author dnpetrov
 */
public class MultiReadMultiWriteMutex {

    // Missing docs: negative values are # of read locks, positive — # of write locks
    private final AtomicInteger rwCount = new AtomicInteger(0);

    /**
     * Waits until it is possible to acquire write lock.
     *
     * <p>Should always be used in the following way:</p>
     * <p>
     * <code>
     *     m.acquireWriteLock();
     *     try {
     *         // do writes
     *     } finally {
     *         m.releaseWriteLock();
     *     }
     * </code>
     * </p>
     */
    private void acquireWriteLock() {
        // Busy wait loop. Why?
        while (true) {
            int oldCount = rwCount.get();
            if (oldCount < 0) {
                // spin until read locks are released
                continue;
            }
            if (rwCount.compareAndSet(oldCount, oldCount + 1)) {
                break;
            }
        }
    }

    /**
     * Releases a single write lock.
     *
     * <p>Should always be used in the following way:</p>
     * <p>
     * <code>
     *     m.acquireWriteLock();
     *     try {
     *         // do writes
     *     } finally {
     *         m.releaseWriteLock();
     *     }
     * </code>
     * </p>
     */
    private void releaseWriteLock() {
        rwCount.decrementAndGet();
    }

    /**
     * Waits until it is possible to acquire read lock.
     *
     * <p>Should always be used in the following way:</p>
     * <p>
     * <code>
     *     m.acquireReadLock();
     *     try {
     *         // do writes
     *     } finally {
     *         m.releaseReadLock();
     *     }
     * </code>
     * </p>
     */
    public void acquireReadLock() {
        // Another busy wait loop
        while (true) {
            int oldCount = rwCount.get();
            if (oldCount > 0) {
                // spin until write locks are dispose
                continue;
            }
            if (rwCount.compareAndSet(oldCount, oldCount-1)) {
                break;
            }
        }
    }

    /**
     * Releases a single read lock.
     *
     * <p>Should always be used in the following way:</p>
     * <p>
     * <code>
     *     m.acquireReadLock();
     *     try {
     *         // do writes
     *     } finally {
     *         m.releaseReadLock();
     *     }
     * </code>
     * </p>
     */
    public void releaseReadLock() {
        rwCount.incrementAndGet();
    }

    /**
     * Performs an action under a write lock.
     *
     * @param r action to perform
     */
    public void withWriteLock(Runnable r) {
        acquireWriteLock();
        try {
            r.run();
        } finally {
            releaseWriteLock();
        }
    }

    /**
     * Performs an action under a read lock.
     *
     * @param r action to perform
     */
    public void withReadLock(Runnable r) {
        acquireReadLock();
        try {
            r.run();
        } finally {
            releaseReadLock();
        }
    }

    /**
     * Computes some value under a write lock.
     *
     * @param f value supplier
     * @param <T> value type
     * @return computed value
     */
    public <T> T withWriteLock(Supplier<T> f) {
        acquireWriteLock();
        try {
            return f.get();
        } finally {
            releaseWriteLock();
        }
    }

    /**
     * Computes some value under a read lock.
     *
     * @param f value supplier
     * @param <T> value type
     * @return computed value
     */
    public <T> T withReadLock(Supplier<T> f) {
        acquireReadLock();
        try {
            return f.get();
        } finally {
            releaseReadLock();
        }
    }

}
