package net.alaarc.interpreter;

import net.alaarc.AlaarcOptions;
import net.alaarc.vm.*;

import java.io.IOException;

/**
 * @author dnpetrov
 */
public class AlaarcInterpreter {
    private final AlaarcOptions options;
    private final VmProgram vmProgram;
    private int assertionsPassed;
    private int assertionsFailed;
    private int alaarcExceptions;

    public AlaarcInterpreter(AlaarcOptions options, VmProgram vmProgram) {
        this.options = options;
        this.vmProgram = vmProgram;
    }

    public void run() {
        InterpreterEnvironment exec;
        try {
            exec = new InterpreterEnvironment(options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        VmProgramInterpreter vmProgramInterpreter = new VmProgramInterpreter(exec, vmProgram);

        assertionsPassed = 0;
        assertionsFailed = 0;
        alaarcExceptions = 0;

        int times = options.getTimes();
        for (int i = 0; i < times; ++i) {
            vmProgramInterpreter.run();
            exec.waitUntilDone();
            int passed = exec.getAssertionsPassedCount();
            int failed = exec.getAssertionsFailedCount();
            int exns = exec.getVmExceptionsCount();
            exec.postMessage("\n\tRun " + (i + 1) + " of " + times
                    + "\n\tAssertions passed: " + passed
                    + "\n\tAssertions failed: " + failed
                    + "\n\tExceptions: " + exns
            );
            assertionsPassed += passed;
            assertionsFailed += failed;
            alaarcExceptions += exns;
            vmProgram.reset();
        }

        System.out.println("--- DONE ---");
        System.out.println("Total assertions passed: " + assertionsPassed);
        System.out.println("Total assertions failed: " + assertionsFailed);
        System.out.println("Total exceptions: " + alaarcExceptions);
    }

    public int getAssertionsPassed() {
        return assertionsPassed;
    }

    public int getAssertionsFailed() {
        return assertionsFailed;
    }

    public int getAlaarcExceptions() {
        return alaarcExceptions;
    }

    public boolean hadAnyProblems() {
        return assertionsFailed != 0 || alaarcExceptions != 0;
    }
}
