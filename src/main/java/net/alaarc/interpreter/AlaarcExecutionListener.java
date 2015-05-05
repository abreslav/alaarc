package net.alaarc.interpreter;

import net.alaarc.AlaarcOptions;
import net.alaarc.IAlaarcEventsListener;
import net.alaarc.log.Logger;
import net.alaarc.vm.AlaarcEventsLogger;
import net.alaarc.vm.VmException;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.instructions.AssertRc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dnpetrov
 */
public class AlaarcExecutionListener implements IAlaarcEventsListener {
    private final AtomicInteger assertionsPassed = new AtomicInteger(0);
    private final AtomicInteger assertionsFailed = new AtomicInteger(0);
    private final AtomicInteger vmExceptionsCount = new AtomicInteger(0);

    private final AlaarcEventsLogger vmEventsLogger;
    private final Logger logger;

    private final List<RunResult> runResults = new ArrayList<>();

    private int totalAssertionsPassed;
    private int totalAssertionsFailed;
    private int totalVmExceptions;

    public AlaarcExecutionListener(AlaarcOptions options) throws IOException {
        logger = resolveLogger(options);
        vmEventsLogger = new AlaarcEventsLogger(logger);
    }

    private static Logger resolveLogger(AlaarcOptions options) throws IOException {
        if (options.getLogFileName().isPresent()) {
            String logFileName = options.getLogFileName().get();
            return new Logger(new PrintWriter(AlaarcOptions.resolveOutputStream(logFileName)));
        } else {
            return new Logger();
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
        vmEventsLogger.onProgramStarted();
    }

    @Override
    public void onProgramFinished() {
        vmEventsLogger.onProgramFinished();
    }

    @Override
    public void onObjectDisposed(long objectId) {
        vmEventsLogger.onObjectDisposed(objectId);
    }

    @Override
    public void onJavaException(VmInstruction instr, Exception e) {
        vmEventsLogger.onJavaException(instr, e);
        throw new RuntimeException(e);
    }

    @Override
    public void onVmException(VmInstruction instr, VmException e) {
        vmEventsLogger.onVmException(instr, e);
        vmExceptionsCount.incrementAndGet();
    }

    @Override
    public void onThreadSpawned(VmInstruction instr, String threadName) {
        vmEventsLogger.onThreadSpawned(instr, threadName);
    }

    @Override
    public void onThreadFinished(String threadName) {
        vmEventsLogger.onThreadFinished(threadName);
    }

    @Override
    public void onObjectDump(VmInstruction instr, String dump) {
        vmEventsLogger.onObjectDump(instr, dump);
    }

    @Override
    public void onPostMessage(VmInstruction instr, String message) {
        vmEventsLogger.onPostMessage(instr, message);
    }

    @Override
    public void onAssertionPassed(AssertRc instr, long actualRc) {
        vmEventsLogger.onAssertionPassed(instr, actualRc);
        assertionsPassed.incrementAndGet();
    }

    @Override
    public void onAssertionFailed(AssertRc instr, long actualRc) {
        vmEventsLogger.onAssertionFailed(instr, actualRc);
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

        RunResult result = new RunResult(i, runAssertionsPassed, runAssertionsFailed, runVmExceptions);
        runResults.add(result);

        totalAssertionsPassed += runAssertionsPassed;
        totalAssertionsFailed += runAssertionsFailed;
        totalVmExceptions += runVmExceptions;
    }

    @Override
    public void onHarnessFinished() {
        logger.finish();
    }

    public List<RunResult> getRunResults() {
        return runResults;
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
