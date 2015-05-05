package net.alaarc.interpreter;

import net.alaarc.AlaarcOptions;
import net.alaarc.ComposedAlaarcListener;
import net.alaarc.RunStatisticsCollector;
import net.alaarc.log.Logger;
import net.alaarc.vm.AlaarcLogger;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author dnpetrov
 */
public class AlaarcExecutionListener extends ComposedAlaarcListener {
    private final AlaarcLogger alaarcEventsLogger;
    private final RunStatisticsCollector runStatisticsCollector = new RunStatisticsCollector();

    public AlaarcExecutionListener(AlaarcOptions options) throws IOException {
        alaarcEventsLogger = new AlaarcLogger(resolveLogger(options));

        addChildListener(alaarcEventsLogger);
        addChildListener(runStatisticsCollector);
    }

    private static Logger resolveLogger(AlaarcOptions options) throws IOException {
        if (options.getLogFileName().isPresent()) {
            String logFileName = options.getLogFileName().get();
            return new Logger(new PrintWriter(AlaarcOptions.resolveOutputStream(logFileName)));
        } else {
            return new Logger();
        }
    }

    public List<RunResult> getRunResults() {
        return runStatisticsCollector.getRunResults();
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
