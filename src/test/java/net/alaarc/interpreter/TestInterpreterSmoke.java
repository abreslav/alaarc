package net.alaarc.interpreter;

import net.alaarc.FileTestUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author dnpetrov
 */
public class TestInterpreterSmoke {
    @Test
    public void testInterpreterBasic() throws IOException {
        InterpreterTestResult result = InterpreterTestUtils.runProgram("/test1-sync.alaarc", 1);
        InterpreterTestUtils.assertNoProblems(result);
        Assert.assertEquals(1, result.getTotalAssertionsPassed());
        FileTestUtils.assertContentEquals("/test1-sync.golden.alaarc-trace", result.getTrace());
    }

    @Test
    public void testExamplesHello() throws IOException {
        InterpreterTestResult result = InterpreterTestUtils.runProgram("/hello.alaarc", 1);
        InterpreterTestUtils.assertNoProblems(result);
        FileTestUtils.assertContentEquals("/hello.golden.alaarc-trace", result.getTrace());
    }

    @Test
    public void testExamplesCyclic() throws IOException {
        InterpreterTestResult result = InterpreterTestUtils.runProgram("/cyclic.alaarc", 1);
        InterpreterTestUtils.assertNoProblems(result);
        Assert.assertEquals(2, result.getTotalAssertionsPassed());
        Assert.assertEquals(1, result.getTotalLiveObjects());
    }

    @Test
    public void testExamplesException() throws IOException {
        InterpreterTestResult result = InterpreterTestUtils.runProgram("/test-exception.alaarc", 1);
        Assert.assertEquals(3, result.getTotalVmExceptions());
    }


}
