package net.alaarc.vm;

import java.util.List;
import java.util.Objects;

/**
 * "Program" for VM consists of a main thread (that, in turn, contains child threads), and global variables.
 * Global variables are mutable objects referenced from instructions within threads.
 *
 * @author dnpetrov
 */
public class VmProgram {
    private final List<VmGlobalVar> globalVars;
    private final VmThreadDef mainThread;

    public VmProgram(List<VmGlobalVar> globalVars, VmThreadDef mainThread) {
        this.globalVars = Objects.requireNonNull(globalVars);
        this.mainThread = Objects.requireNonNull(mainThread);
    }

    public VmThreadDef getMainThread() {
        return mainThread;
    }


}
