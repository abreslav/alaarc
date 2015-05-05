package net.alaarc.interpreter;

/**
 * @author dnpetrov
 */
public class InterpreterTestResult {
    private final String logContent;
    private final int totalAssertionsPassed;
    private final int totalAssertionsFailed;
    private final int totalVmExceptions;

    public InterpreterTestResult(String logContent, int totalAssertionsPassed, int totalAssertionsFailed, int totalVmExceptions) {
        this.logContent = logContent;
        this.totalAssertionsPassed = totalAssertionsPassed;
        this.totalAssertionsFailed = totalAssertionsFailed;
        this.totalVmExceptions = totalVmExceptions;
    }

    public String getLogContent() {
        return logContent;
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
