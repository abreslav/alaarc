package net.alaarc.interpreter;

import net.alaarc.ComposedAlaarcListener;
import net.alaarc.RunStatisticsCollector;
import net.alaarc.vm.AlaarcLogger;

/**
 * TODO code duplication with {@link AlaarcExecutionListener} & {@link AlaarcLogger}, refactor.
 *
 * @author dnpetrov
 */
public class InterpreterTestListener extends ComposedAlaarcListener {
    private final RunStatisticsCollector runStatisticsCollector = new RunStatisticsCollector();
    private final InterpreterTestTracer tracer = new InterpreterTestTracer();

    public InterpreterTestListener() {
        addChildListener(runStatisticsCollector);
        addChildListener(tracer);
    }

    public InterpreterTestResult getTestResult() {
        return new InterpreterTestResult(
                tracer.getTrace(),
                runStatisticsCollector.getTotalAssertionsPassed(),
                runStatisticsCollector.getTotalAssertionsFailed(),
                runStatisticsCollector.getTotalLiveObjects(),
                runStatisticsCollector.getTotalVmExceptions());
    }

    public String getLogContent() {
        return tracer.getTrace();
    }

    public int getTotalAssertionsPassed() {
        return runStatisticsCollector.getTotalAssertionsPassed();
    }

    public int getTotalAssertionsFailed() {
        return runStatisticsCollector.getTotalAssertionsFailed();
    }

    public int getTotalVmExceptions() {
        return runStatisticsCollector.getTotalVmExceptions();
    }
}
