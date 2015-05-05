package net.alaarc.compiler;

import net.alaarc.AntlrTestUtils;
import net.alaarc.FileTestUtils;
import net.alaarc.ast.AstDumper;
import net.alaarc.ast.nodes.AstProgram;
import net.alaarc.vm.VmProgram;
import org.junit.Assert;

import java.io.IOException;

/**
 * @author dnpetrov
 */
public class CompilerTestUtils {
    public static AlaarcCompiler compileProgram(String path) throws IOException {
        AlaarcCompiler compiler = new AlaarcCompiler(path, AntlrTestUtils.getResourceAsAntlrInput(path));
        compiler.run();

        AstProgram astProgram = compiler.getAstProgram();
        if (astProgram != null) {
            System.out.println("> AST for " + path + ": ");
            AstDumper.dump(System.out, astProgram);
        }

        VmProgram vmProgram = compiler.getVmProgram();
        if (vmProgram != null) {
            System.out.println("> VM code for " + path + ": ");
            VmProgram.dump(System.out, vmProgram);
        }

        return compiler;
    }

    public static void assertEqualsGolden(String pathToGoldenFile, AstProgram astProgram) {
        Assert.assertNotNull("astProgram should be non-null", astProgram);
        String astProgramStr = AstDumper.dumpToString(astProgram);
        FileTestUtils.assertContentEquals(pathToGoldenFile, astProgramStr);
    }

    public static void assertEqualsGolden(String pathToGoldenFile, VmProgram vmProgram) {
        Assert.assertNotNull("vmProgram should be non-null", vmProgram);
        String vmProgramStr = VmProgram.dumpToString(vmProgram);
        FileTestUtils.assertContentEquals(pathToGoldenFile, vmProgramStr);
    }
}
