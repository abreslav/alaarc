package net.alaarc.interpreter;

import net.alaarc.IAlaarcEventsListener;
import net.alaarc.log.ILogMessageFormatter;
import net.alaarc.log.LogMessage;
import net.alaarc.log.Logger;
import net.alaarc.vm.AlaarcEventsLogger;
import net.alaarc.vm.VmException;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.instructions.AssertRc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO code duplication with {@link AlaarcExecutionListener} & {@link AlaarcEventsLogger}, refactor.
 *
 * @author dnpetrov
 */
public class InterpreterTestListener implements IAlaarcEventsListener {
    private final StringWriter traceWriter;
    private final Logger traceLogger;

    private final AtomicInteger assertionsPassed = new AtomicInteger(0);
    private final AtomicInteger assertionsFailed = new AtomicInteger(0);
    private final AtomicInteger vmExceptionsCount = new AtomicInteger(0);

    private int totalAssertionsPassed;
    private int totalAssertionsFailed;
    private int totalVmExceptions;

    private static ILogMessageFormatter TEST_MESSAGE_FORMATTER = LogMessage::getMessage;

    public InterpreterTestListener() {
        traceWriter = new StringWriter();
        traceLogger = new Logger(new PrintWriter(traceWriter, true), TEST_MESSAGE_FORMATTER);
    }

    private void log(String message) {
        try {
            traceLogger.log(LogMessage.create(message));
        } catch (InterruptedException e) {
            // swallow it
        }
    }

    @Override
    public void onHarnessStarted() {
        totalAssertionsPassed = 0;
        totalAssertionsFailed = 0;
        totalVmExceptions = 0;
    }

    @Override
    public void onProgramStarted() {
        // do nothing
    }

    @Override
    public void onProgramFinished() {
        // do nothing
    }

    @Override
    public void onObjectDisposed(long objectId) {
        // do nothing
    }

    @Override
    public void onJavaException(VmInstruction instr, Exception e) {
        throw new RuntimeException(e);
    }

    @Override
    public void onVmException(VmInstruction instr, VmException e) {
        vmExceptionsCount.incrementAndGet();
        log(e.getMessage());
    }

    @Override
    public void onThreadSpawned(VmInstruction instr, String threadName) {
        // do nothing
    }

    @Override
    public void onThreadFinished(String threadName) {
        // do nothing
    }

    @Override
    public void onObjectDump(VmInstruction instr, String dump) {
        log(instr.toString() + ": " + dump);
    }

    @Override
    public void onPostMessage(VmInstruction instr, String message) {
        log(instr.toString() + ": " + message);
    }

    @Override
    public void onAssertionPassed(AssertRc instr, long actualRc) {
        log("@" + instr.getDebugInfo() + ": assertion PASSED: "
                + actualRc + instr.getComparisonOperator() + instr.getNumber());
        assertionsPassed.incrementAndGet();
    }

    @Override
    public void onAssertionFailed(AssertRc instr, long actualRc) {
        log("@" + instr.getDebugInfo() + ": assertion FAILED: "
                + actualRc + instr.getComparisonOperator() + instr.getNumber());
        assertionsFailed.incrementAndGet();
    }

    @Override
    public void onRunStarted(int i) {
        assertionsPassed.set(0);
        assertionsFailed.set(0);
        vmExceptionsCount.set(0);
    }

    @Override
    public void onRunFinished(int i) {
        int runAssertionsPassed = assertionsPassed.get();
        int runAssertionsFailed = assertionsFailed.get();
        int runVmExceptions = vmExceptionsCount.get();

        totalAssertionsPassed += runAssertionsPassed;
        totalAssertionsFailed += runAssertionsFailed;
        totalVmExceptions += runVmExceptions;
    }

    @Override
    public void onHarnessFinished() {
        traceLogger.finish();
    }

    public InterpreterTestResult getTestResult() {
        return new InterpreterTestResult(getLogContent(),
                getTotalAssertionsPassed(), getTotalAssertionsFailed(), getTotalVmExceptions());
    }

    public String getLogContent() {
        return traceWriter.toString();
    }

    public int getTotalAssertionsPassed() {
        return totalAssertionsPassed;
    }

    public int getTotalAssertionsFailed() {
        return totalAssertionsFailed;
    }

    public int getTotalVmExceptions() {
        return totalVmExceptions;
    }
}
