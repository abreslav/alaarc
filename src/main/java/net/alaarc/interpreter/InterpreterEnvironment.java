package net.alaarc.interpreter;

import net.alaarc.AlaarcOptions;
import net.alaarc.log.LogMessage;
import net.alaarc.log.Logger;
import net.alaarc.vm.IVmEventsListener;
import net.alaarc.vm.VmEventsLogger;
import net.alaarc.vm.VmException;
import net.alaarc.vm.instructions.AssertRc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author dnpetrov
 */
public class InterpreterEnvironment implements IVmEventsListener {
    private final AtomicInteger threadsCount = new AtomicInteger(0);

    private final VmEventsLogger vmEventsLogger;
    private final Logger logger;

    private final Lock executionLock = new ReentrantLock();
    private final Condition executionDone = executionLock.newCondition();

    private final AtomicInteger assertionsPassed = new AtomicInteger(0);
    private final AtomicInteger assertionsFailed = new AtomicInteger(0);

    private volatile boolean finished = false;

    private final List<VmException> vmExceptions = new ArrayList<>();

    public InterpreterEnvironment(AlaarcOptions options) throws IOException {
        logger = resolveLogger(options);
        vmEventsLogger = new VmEventsLogger(resolveLogger(options));
    }

    private static Logger resolveLogger(AlaarcOptions options) throws IOException {
        if (options.getLogFileName().isPresent()) {
            String logFileName = options.getLogFileName().get();
            return new Logger(new PrintWriter(options.resolveOutputStream(logFileName)));
        } else {
            return new Logger();
        }
    }

    public void postMessage(String message) {
        try {
            logger.log(LogMessage.create(message));
        } catch (InterruptedException e) {
            // swallow it
        }
    }

    public int getAssertionsPassedCount() {
        return assertionsPassed.get();
    }

    public int getAssertionsFailedCount() {
        return assertionsFailed.get();
    }

    @Override
    public void onObjectDisposed(long objectId) {
        vmEventsLogger.onObjectDisposed(objectId);
    }

    @Override
    public void onJavaException(Exception e) {
        vmEventsLogger.onJavaException(e);
        throw new RuntimeException(e);
    }

    @Override
    public void onVmException(VmException e) {
        vmEventsLogger.onVmException(e);
        vmExceptions.add(e);
    }

    @Override
    public void onThreadStart(int threadId) {
        vmEventsLogger.onThreadStart(threadId);
        threadsCount.incrementAndGet();
    }

    @Override
    public void onThreadFinish(int threadId) {
        vmEventsLogger.onThreadFinish(threadId);
        int numThreads = threadsCount.decrementAndGet();
        if (numThreads == 0) {
            vmEventsLogger.finish();
            finished = true;
            executionLock.lock();
            executionDone.signalAll();
            executionLock.unlock();
        }
    }

    @Override
    public void onObjectDump(String dump) {
        vmEventsLogger.onObjectDump(dump);
    }

    @Override
    public void onPostMessage(String message) {
        vmEventsLogger.onPostMessage(message);
    }

    @Override
    public void onAssertionPassed(AssertRc instr) {
        vmEventsLogger.onAssertionPassed(instr);
        assertionsPassed.incrementAndGet();
    }

    @Override
    public void onAssertionFailed(AssertRc instr) {
        vmEventsLogger.onAssertionFailed(instr);
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

    public void finish() {
        logger.finish();
    }
}
