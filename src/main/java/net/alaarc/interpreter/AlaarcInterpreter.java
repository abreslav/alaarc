package net.alaarc.interpreter;

import net.alaarc.AlaarcOptions;
import net.alaarc.vm.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dnpetrov
 */
public class AlaarcInterpreter {
    private static class RunResult {
        private final int index;
        private final int assertionsPassed;
        private final int assertionsFailed;
        private final int alaarcExceptions;

        public RunResult(int index, int assertionsPassed, int assertionsFailed, int alaarcExceptions) {
            this.index = index;
            this.assertionsPassed = assertionsPassed;
            this.assertionsFailed = assertionsFailed;
            this.alaarcExceptions = alaarcExceptions;
        }

        public int getIndex() {
            return index;
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

        @Override
        public String toString() {
            return "Run " + index + ": " +
                    "assertions passed: " + assertionsPassed + "; " +
                    "assertions failed: " + assertionsFailed + "; " +
                    "exceptions: " + alaarcExceptions
                    ;
        }
    }

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
        AlaarcExecutionListener exec;
        try {
            exec = new AlaarcExecutionListener(options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        VmProgramInterpreter vmInterpreter = new VmProgramInterpreter(exec, vmProgram);

        assertionsPassed = 0;
        assertionsFailed = 0;
        alaarcExceptions = 0;

        List<RunResult> runResults = new ArrayList<>();

        int times = options.getTimes();
        for (int i = 0; i < times; ++i) {
            vmInterpreter.run();
            exec.waitUntilDone();

            int passed = exec.getAssertionsPassedCount();
            int failed = exec.getAssertionsFailedCount();
            int exns = exec.getVmExceptionsCount();

            runResults.add(new RunResult(i, passed, failed, exns));

            assertionsPassed += passed;
            assertionsFailed += failed;
            alaarcExceptions += exns;

            vmProgram.reset();
            exec.reset();
        }

        exec.finish();

        System.out.println("--- DONE ---");

        runResults.forEach(System.out::println);

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
