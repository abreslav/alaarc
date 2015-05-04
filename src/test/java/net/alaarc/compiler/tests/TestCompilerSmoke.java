package net.alaarc.compiler.tests;

import net.alaarc.TestUtils;
import net.alaarc.compiler.AlaarcCompiler;
import org.junit.Test;

import java.io.IOException;

/**
 * @author dnpetrov
 */
public class TestCompilerSmoke {
    // Major. Tests are basically absent

    @Test
    public void testCompile0() throws Exception {
        // Does not seem to test much either
        compileProgram("/test1.alaarc");
    }

    private void compileProgram(String path) throws IOException {
        AlaarcCompiler compiler = new AlaarcCompiler(path, TestUtils.getResourceAsAntlrInput(path));
        compiler.run();
    }


}
