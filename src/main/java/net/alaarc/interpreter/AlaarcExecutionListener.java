package net.alaarc.interpreter;

import net.alaarc.AlaarcOptions;
import net.alaarc.ComposedAlaarcListener;
import net.alaarc.RunStatusesCollector;
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
    private final RunStatusesCollector runStatusesCollector = new RunStatusesCollector();

    public AlaarcExecutionListener(AlaarcOptions options) throws IOException {
        alaarcEventsLogger = new AlaarcLogger(resolveLogger(options));

        addChildListener(alaarcEventsLogger);
        addChildListener(runStatusesCollector);
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
        return runStatusesCollector.getRunResults();
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
