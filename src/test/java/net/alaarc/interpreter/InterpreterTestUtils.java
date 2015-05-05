package net.alaarc.interpreter;

import net.alaarc.AlaarcOptions;
import net.alaarc.compiler.AlaarcCompiler;
import net.alaarc.compiler.CompilerTestUtils;
import net.alaarc.vm.VmProgram;
import org.junit.Assert;

import java.io.IOException;

/**
 * @author dnpetrov
 */
public class InterpreterTestUtils {
    /**
     *
     * @param path  path to program source file (under <code>test/jave/resources</code>)
     * @param times times to run
     * @return      summary of interpreter test results
     *
     * @throws IOException  if any of the required files are not found
     */
    public static InterpreterTestResult runProgram(String path, int times) throws IOException {
        AlaarcCompiler compiler = CompilerTestUtils.compileProgram(path);

        return runProgram(compiler.getVmProgram(), times);
    }

    public static InterpreterTestResult runProgram(VmProgram program, int times) {
        Assert.assertNotNull("Compilation failure", program);

        AlaarcInterpreter interpreter = createAlaarcInterpreter(program, times);
        InterpreterTestListener listener = new InterpreterTestListener();
        interpreter.runWith(listener);

        System.out.println("> Execution trace for " + program.getSourceFileName() + ":");
        System.out.println(listener.getLogContent());

        System.out.println("Total assertions passed: " + listener.getTotalAssertionsPassed());
        System.out.println("Total assertions failed: " + listener.getTotalAssertionsFailed());
        System.out.println("Total exceptions: " + listener.getTotalVmExceptions());

        return listener.getTestResult();
    }

    private static AlaarcInterpreter createAlaarcInterpreter(VmProgram program, int times) {
        AlaarcOptions options = new AlaarcOptions();
        options.setTimes(times);
        return new AlaarcInterpreter(options, program);
    }

    public static void assertNoProblems(InterpreterTestResult testResult) {
        Assert.assertEquals("Some assertions failed", 0, testResult.getTotalAssertionsFailed());
        Assert.assertEquals("Some VM exceptions fired", 0, testResult.getTotalVmExceptions());
    }
}
