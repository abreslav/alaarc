package net.alaarc.vm;

import net.alaarc.DefaultAlaarcListener;
import net.alaarc.log.LogMessage;
import net.alaarc.log.Logger;
import net.alaarc.vm.instructions.AssertRc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * @author dnpetrov
 */
public class AlaarcLogger extends DefaultAlaarcListener {
    private final Logger logger;

    public AlaarcLogger(Logger logger) {
        this.logger = Objects.requireNonNull(logger);
    }

    private void log(String message) {
        try {
            logger.log(LogMessage.create(message));
        } catch (InterruptedException e) {
            // swallow it
        }
    }

    @Override
    public void onProgramStarted() {
        log("Program started");
    }

    @Override
    public void onProgramFinished() {
        log("Program finished");
    }

    @Override
    public void onObjectDisposed(long objectId) {
        log("Object disposed: " + objectId);
    }

    @Override
    public void onJavaException(VmInstruction instr, Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        log("@" + instr.getDebugInfo() + ": Java exception: " + e.getMessage() + "\n" + sw.toString());
    }

    @Override
    public void onVmException(VmInstruction instr, VmException e) {
        log("@" + instr.getDebugInfo() + ": Alaarc exception: " + e.getMessage());
    }

    @Override
    public void onThreadSpawned(VmInstruction instr, String threadName) {
        if (instr == null) {
            log("Main thread spawned: " + threadName);
        } else {
            log("@" + instr.getDebugInfo() +": Thread spawned: " + threadName);
        }
    }

    @Override
    public void onThreadFinished(String threadName) {
        log("Thread finished: " + threadName);
    }

    @Override
    public void onObjectDump(VmInstruction instr, String dump) {
        log("@" + instr.getDebugInfo() + ": " + dump);
    }

    @Override
    public void onPostMessage(VmInstruction instr, String message) {
        log("@" + instr.getDebugInfo() + ": " + message);
    }

    @Override
    public void onAssertionPassed(AssertRc instr, long actualRc) {
        log("@" + instr.getDebugInfo() + ": assertion PASSED: "
                + actualRc + instr.getComparisonOperator() + instr.getNumber());
    }

    @Override
    public void onAssertionFailed(AssertRc instr, long actualRc) {
        log("@" + instr.getDebugInfo() + ": assertion FAILED: "
                + actualRc + instr.getComparisonOperator() + instr.getNumber());
    }

    @Override
    public void onHarnessFinished() {
        logger.finish();
    }
}
