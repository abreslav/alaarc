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

        int times = options.getTimes();
        for (int i = 0; i < times; ++i) {
            vmProgramInterpreter.run();
            exec.waitUntilDone();
            int passed = exec.getAssertionsPassedCount();
            int failed = exec.getAssertionsFailedCount();
            exec.postMessage("Run " + (i + 1) + " of " + times);
            exec.postMessage("Assertions passed: " + passed);
            exec.postMessage("Assertions failed: " + failed);
            assertionsPassed += passed;
            assertionsFailed += failed;
            vmProgram.reset();
        }

        exec.postMessage("--- DONE ---");
        exec.postMessage("Total assertions passed: " + assertionsPassed);
        exec.postMessage("Total assertions failed: " + assertionsFailed);
        exec.finish();
    }

    public int getAssertionsPassed() {
        return assertionsPassed;
    }

    public int getAssertionsFailed() {
        return assertionsFailed;
    }
}
