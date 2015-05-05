package net.alaarc.interpreter;

import net.alaarc.ComposedAlaarcListener;
import net.alaarc.IAlaarcListener;
import net.alaarc.RunStatusesCollector;
import net.alaarc.log.ILogMessageFormatter;
import net.alaarc.log.LogMessage;
import net.alaarc.log.Logger;
import net.alaarc.vm.AlaarcLogger;
import net.alaarc.vm.VmException;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.instructions.AssertRc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO code duplication with {@link AlaarcExecutionListener} & {@link AlaarcLogger}, refactor.
 *
 * @author dnpetrov
 */
public class InterpreterTestListener extends ComposedAlaarcListener {
    private final RunStatusesCollector runStatusesCollector = new RunStatusesCollector();
    private final InterpreterTestTracer tracer = new InterpreterTestTracer();

    public InterpreterTestListener() {
        addChildListener(runStatusesCollector);
        addChildListener(tracer);
    }

    public InterpreterTestResult getTestResult() {
        return new InterpreterTestResult(
                tracer.getTrace(),
                runStatusesCollector.getTotalAssertionsPassed(),
                runStatusesCollector.getTotalAssertionsFailed(),
                runStatusesCollector.getTotalVmExceptions());
    }

    public String getLogContent() {
        return tracer.getTrace();
    }

    public int getTotalAssertionsPassed() {
        return runStatusesCollector.getTotalAssertionsPassed();
    }

    public int getTotalAssertionsFailed() {
        return runStatusesCollector.getTotalAssertionsFailed();
    }

    public int getTotalVmExceptions() {
        return runStatusesCollector.getTotalVmExceptions();
    }
}
