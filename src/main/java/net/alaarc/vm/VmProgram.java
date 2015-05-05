package net.alaarc.vm;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Objects;

/**
 * "Program" for VM consists of a main thread (that, in turn, contains child threads), and global variables.
 *
 * @author dnpetrov
 */
public class VmProgram {
    private final String sourceFileName;
    private final List<VmGlobalVarDef> globalVarDefs;
    private final VmThreadDef mainThread;
    private final List<VmThreadDef> vmThreadDefs;

    public VmProgram(String sourceFileName, List<VmGlobalVarDef> globalVarDefs, VmThreadDef mainThread, List<VmThreadDef> vmThreadDefs) {
        this.sourceFileName = Objects.requireNonNull(sourceFileName);
        this.globalVarDefs = Objects.requireNonNull(globalVarDefs);
        this.mainThread = Objects.requireNonNull(mainThread);
        this.vmThreadDefs = Objects.requireNonNull(vmThreadDefs);
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public VmThreadDef getMainThread() {
        return mainThread;
    }

    public List<VmGlobalVarDef> getGlobalVarDefs() {
        return globalVarDefs;
    }

    public List<VmThreadDef> getVmThreadDefs() {
        return vmThreadDefs;
    }

    public void dump(PrintWriter pw) {
        pw.println("Global variables: ");
        for (VmGlobalVarDef gv : globalVarDefs) {
            pw.println("  " + gv);
        }
        pw.println();

        for (VmThreadDef threadDef : vmThreadDefs) {
            threadDef.dump(pw);
            pw.println();
        }
    }
    
    public static void dump(OutputStream ps, VmProgram program) {
        PrintWriter pw = new PrintWriter(ps);
        program.dump(pw);
        pw.flush();
    }

    public static String dumpToString(VmProgram vmProgram) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        vmProgram.dump(pw);
        pw.flush();
        return sw.toString();
    }
}
