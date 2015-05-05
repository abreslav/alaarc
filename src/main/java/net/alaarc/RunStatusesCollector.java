package net.alaarc;

import net.alaarc.interpreter.RunResult;
import net.alaarc.vm.VmException;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.instructions.AssertRc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dnpetrov
 */
public class RunStatusesCollector extends NullAlaarcListener {
    private final List<RunResult> runResults = new ArrayList<>();

    private final AtomicInteger assertionsPassed = new AtomicInteger(0);
    private final AtomicInteger assertionsFailed = new AtomicInteger(0);
    private final AtomicInteger vmExceptionsCount = new AtomicInteger(0);

    private int totalAssertionsPassed;
    private int totalAssertionsFailed;
    private int totalVmExceptions;

    @Override
    public void onVmException(VmInstruction instr, VmException e) {
        vmExceptionsCount.incrementAndGet();
    }

    @Override
    public void onAssertionPassed(AssertRc instr, long actualRc) {
        assertionsPassed.incrementAndGet();
    }

    @Override
    public void onAssertionFailed(AssertRc instr, long actualRc) {
        assertionsFailed.incrementAndGet();
    }

    @Override
    public void onHarnessStarted() {
        totalAssertionsPassed = 0;
        totalAssertionsFailed = 0;
        totalVmExceptions = 0;
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
