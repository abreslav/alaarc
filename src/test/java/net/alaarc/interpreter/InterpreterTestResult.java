package net.alaarc.interpreter;

/**
 * @author dnpetrov
 */
public class InterpreterTestResult {
    private final String trace;
    private final int totalAssertionsPassed;
    private final int totalAssertionsFailed;
    private final int totalLiveObjects;
    private final int totalVmExceptions;

    public InterpreterTestResult(String trace, int totalAssertionsPassed, int totalAssertionsFailed,
                                 int totalLiveObjects, int totalVmExceptions) {
        this.trace = trace;
        this.totalAssertionsPassed = totalAssertionsPassed;
        this.totalAssertionsFailed = totalAssertionsFailed;
        this.totalLiveObjects = totalLiveObjects;
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

    public int getTotalLiveObjects() {
        return totalLiveObjects;
    }

    public int getTotalVmExceptions() {
        return totalVmExceptions;
    }

}
