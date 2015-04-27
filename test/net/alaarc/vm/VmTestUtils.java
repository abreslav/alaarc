package net.alaarc.vm;

import net.alaarc.vm.builders.VmProgramBuilder;
import net.alaarc.vm.builders.VmThreadBuilder;

import java.util.function.Consumer;

/**
 * @author dnpetrov
 */
public class VmTestUtils {

    public static void runProgram(VmProgram program) {
        VmTestEnvironment te = new VmTestEnvironment();
        VmProgramInterpreter pi = new VmProgramInterpreter(te, program);
        pi.run();
        te.waitUntilDone();
    }

    public static void runProgram(int count, VmProgram program) {
        for (int i = 0; i < count; ++i) {
            runProgram(program);
        }
    }

    public static void runProgram(Consumer<VmThreadBuilder> mainThread) {
        runProgram(VmProgramBuilder.withMainThread(mainThread));
    }

    public static void runProgram(int count, Consumer<VmThreadBuilder> mainThread) {
        runProgram(count, VmProgramBuilder.withMainThread(mainThread));
    }
}
