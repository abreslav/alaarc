package net.alaarc.compiler.tests;

import net.alaarc.AntlrTestUtils;
import net.alaarc.compiler.AlaarcCompiler;
import org.junit.Test;

import java.io.IOException;

/**
 * @author dnpetrov
 */
public class TestCompilerSmoke {

    @Test
    public void testCompile0() throws Exception {
        compileProgram("/test1.alaarc");
    }

    @Test
    public void testCompileError() throws Exception {
        compileProgram("/test-error.alaarc");
    }

    private void compileProgram(String path) throws IOException {
        AlaarcCompiler compiler = new AlaarcCompiler(path, AntlrTestUtils.getResourceAsAntlrInput(path));
        compiler.run();
    }


}
