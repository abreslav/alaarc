package net.alaarc.vm.tests;

import net.alaarc.log.Logger;
import net.alaarc.vm.IVmEventsListener;
import net.alaarc.vm.VmEventsLogger;
import net.alaarc.vm.VmException;
import net.alaarc.vm.instructions.AssertRc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dnpetrov
 */
class VmTestEnvironment implements IVmEventsListener {
    private final AtomicInteger threadsCount = new AtomicInteger(0);

    private final VmEventsLogger logger = new VmEventsLogger(new Logger());

    private final Lock executionLock = new ReentrantLock();
    private final Condition executionDone = executionLock.newCondition();

    private final AtomicInteger assertionsPassed = new AtomicInteger(0);
    private final AtomicInteger assertionsFailed = new AtomicInteger(0);

    private volatile boolean finished = false;

    private final List<VmException> vmExceptions = new ArrayList<>();

    public void postMessage(String message) {
        logger.onPostMessage(message);
    }

    public int getAssertionsPassedCount() {
        return assertionsPassed.get();
    }

    public int getAssertionsFailedCount() {
        return assertionsFailed.get();
    }

    @Override
    public void onObjectDisposed(long objectId) {
        logger.onObjectDisposed(objectId);
    }

    @Override
    public void onJavaException(Exception e) {
        logger.onJavaException(e);
        throw new RuntimeException(e);
    }

    @Override
    public void onVmException(VmException e) {
        logger.onVmException(e);
        vmExceptions.add(e);
    }

    @Override
    public void onThreadStart(int threadId) {
        logger.onThreadStart(threadId);
        threadsCount.incrementAndGet();
    }

    @Override
    public void onThreadFinish(int threadId) {
        logger.onThreadFinish(threadId);
        int numThreads = threadsCount.decrementAndGet();
        if (numThreads == 0) {
            logger.finish();
            finished = true;
            executionLock.lock();
            executionDone.signalAll();
            executionLock.unlock();
        }
    }

    @Override
    public void onObjectDump(String dump) {
        logger.onObjectDump(dump);
    }

    @Override
    public void onPostMessage(String message) {
        logger.onPostMessage(message);
    }

    @Override
    public void onAssertionPassed(AssertRc instr) {
        logger.onAssertionPassed(instr);
        assertionsPassed.incrementAndGet();
    }

    @Override
    public void onAssertionFailed(AssertRc instr) {
        logger.onAssertionFailed(instr);
        assertionsFailed.incrementAndGet();
    }

    public void waitUntilDone() {
        executionLock.lock();
        try {
            while (true) {
                try {
                    executionDone.await();
                    if (finished) {
                        break;
                    }
                } catch (InterruptedException e) {
                    // TODO
                }
            }
        } finally {
            executionLock.unlock();
        }
    }

    public List<VmException> getVmExceptions() {
        return vmExceptions;
    }
}
