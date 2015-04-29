package net.alaarc.vm;

import net.alaarc.log.LogMessage;
import net.alaarc.log.Logger;
import net.alaarc.vm.instructions.AssertRc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * @author dnpetrov
 */
public class VmEventsLogger implements IVmEventsListener {
    private final Logger logger;

    public VmEventsLogger(Logger logger) {
        this.logger = Objects.requireNonNull(logger);
    }

    private void log(String message) {
        try {
            logger.log(LogMessage.create(message));
        } catch (InterruptedException e) {
            // swallow it :(
        }
    }

    public void finish() {
        logger.finish();
    }

    @Override
    public void onObjectDisposed(long objectId) {
        log("Object disposed: " + objectId);
    }

    @Override
    public void onJavaException(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        log("Java exception: " + e.getMessage() + "\n" + sw.toString());
    }

    @Override
    public void onVmException(VmException e) {
        StringWriter sw = new StringWriter();
        log("Alaarc exception: " + e.getMessage());
    }

    @Override
    public void onThreadStart(int threadId) {
        log("Thread started, threadId=" + threadId);
    }

    @Override
    public void onThreadFinish(int threadId) {
        log("Thread finished, threadId=" + threadId);
    }

    @Override
    public void onObjectDump(String dump) {
        log("Object dumped: " + dump);
    }

    @Override
    public void onPostMessage(String message) {
        log(message);
    }

    @Override
    public void onAssertionPassed(AssertRc instr) {
        log("Assertion passed: " + instr.toString());
    }

    @Override
    public void onAssertionFailed(AssertRc instr) {
        log("Assertion failed: " + instr.toString());
    }
}
