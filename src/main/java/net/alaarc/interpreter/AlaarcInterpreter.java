package net.alaarc.interpreter;

import net.alaarc.AlaarcOptions;
import net.alaarc.vm.VmProgram;
import net.alaarc.vm.VmProgramInterpreter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author dnpetrov
 */
public class AlaarcInterpreter {
    private final AlaarcOptions options;
    private final VmProgram vmProgram;

    private int totalErrors;

    public AlaarcInterpreter(AlaarcOptions options, VmProgram vmProgram) {
        this.options = Objects.requireNonNull(options);
        this.vmProgram = Objects.requireNonNull(vmProgram);
    }

    public void runWith(IInterpreterListener listener) {
        listener.onRunsStarted();
        int times = options.getTimes();
        for (int i = 0; i < times; ++i) {
            listener.onRunStarted(i);
            VmProgramInterpreter vmInterpreter = new VmProgramInterpreter(listener, vmProgram);
            vmInterpreter.run();
            listener.onRunFinished(i);
        }
        listener.onRunsFinished();
    }

    public void run() {
        AlaarcExecutionListener exec;
        try {
            exec = new AlaarcExecutionListener(options);
        } catch (IOException e) {
            System.out.println("Startup failed: " + e.getMessage());
            totalErrors = 1;
            return;
        }

        runWith(exec);

        reportRuns(exec);
    }

    private void reportRuns(AlaarcExecutionListener exec) {
        System.out.println("--- DONE ---");

        List<RunResult> runResults = exec.getRunResults();
        runResults.forEach(System.out::println);

        System.out.println("Total assertions passed: " + exec.getTotalAssertionsPassed());
        System.out.println("Total assertions failed: " + exec.getTotalAssertionsFailed());
        System.out.println("Total exceptions: " + exec.getTotalVmExceptions());

        totalErrors = exec.getTotalAssertionsFailed() + exec.getTotalVmExceptions();
    }

    public int getTotalErrors() {
        return totalErrors;
    }

    public boolean hasErrors() {
        return totalErrors > 0;
    }
}
