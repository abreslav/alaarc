package net.alaarc.interpreter;

/**
 * @author dnpetrov
 */
public class InterpreterTestResult {
    private final String trace;
    private final int totalAssertionsPassed;
    private final int totalAssertionsFailed;
    private final int totalVmExceptions;

    public InterpreterTestResult(String trace, int totalAssertionsPassed, int totalAssertionsFailed, int totalVmExceptions) {
        this.trace = trace;
        this.totalAssertionsPassed = totalAssertionsPassed;
        this.totalAssertionsFailed = totalAssertionsFailed;
        this.totalVmExceptions = totalVmExceptions;
    }

    public String getTrace() {
        return trace;
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
