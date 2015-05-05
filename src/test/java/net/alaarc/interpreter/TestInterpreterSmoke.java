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
        FileTestUtils.assertContentEquals("/test1-sync.golden.alaarc-trace", result.getLogContent());
        Assert.assertEquals(1, result.getTotalAssertionsPassed());
        Assert.assertEquals(0, result.getTotalAssertionsFailed());
        Assert.assertEquals(0, result.getTotalVmExceptions());
    }
}
