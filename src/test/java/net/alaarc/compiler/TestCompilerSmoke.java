package net.alaarc.compiler;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author dnpetrov
 */
public class TestCompilerSmoke {

    @Test
    public void testCompilerBasic() throws Exception {
        AlaarcCompiler compiler = CompilerTestUtils.compileProgram("/test1.alaarc");
        CompilerTestUtils.assertEqualsGolden("/test1.golden.alaarc-ast", compiler.getAstProgram());
        CompilerTestUtils.assertEqualsGolden("/test1.golden.alaarc-vm", compiler.getVmProgram());
    }

    @Test
    public void testCompileError() throws Exception {
        AlaarcCompiler compiler = CompilerTestUtils.compileProgram("/test-error.alaarc");
        Assert.assertTrue(compiler.hasErrors());
    }

}
