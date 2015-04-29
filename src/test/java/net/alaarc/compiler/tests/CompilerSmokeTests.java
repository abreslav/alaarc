package net.alaarc.compiler.tests;

import net.alaarc.TestUtils;
import net.alaarc.compiler.AlaarcCompiler;
import org.junit.Test;

import java.io.IOException;

/**
 * @author dnpetrov
 */
public class CompilerSmokeTests {

    @Test
    public void testCompile0() throws Exception {
        compileProgram("/test1.alaarc");
    }

    private void compileProgram(String path) throws IOException {
        AlaarcCompiler compiler = new AlaarcCompiler(path, TestUtils.getResourceAsAntlrInput(path));
        compiler.run();
    }


}
