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
        InterpreterEnvironment interpreterEnvironment;
        try {
            interpreterEnvironment = new InterpreterEnvironment(options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        VmProgramInterpreter vmProgramInterpreter = new VmProgramInterpreter(interpreterEnvironment, vmProgram);

        assertionsPassed = 0;
        assertionsFailed = 0;

        int times = options.getTimes();
        for (int i = 0; i < times; ++i) {
            vmProgramInterpreter.run();
            interpreterEnvironment.waitUntilDone();
            int passed = interpreterEnvironment.getAssertionsPassedCount();
            int failed = interpreterEnvironment.getAssertionsFailedCount();
            System.out.println("Run " + (i+1) + " of " + times);
            System.out.println("Assertions passed: " + passed);
            System.out.println("Assertions failed: " + failed);
            System.out.println();
            assertionsPassed += passed;
            assertionsFailed += failed;
            vmProgram.reset();
        }

        System.out.println("Total assertions passed: " + assertionsPassed);
        System.out.println("Total assertions failed: " + assertionsFailed);
    }

    public int getAssertionsPassed() {
        return assertionsPassed;
    }

    public int getAssertionsFailed() {
        return assertionsFailed;
    }
}
