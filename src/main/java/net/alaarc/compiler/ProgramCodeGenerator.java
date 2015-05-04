package net.alaarc.compiler;

import net.alaarc.ast.nodes.AstProgram;
import net.alaarc.vm.VmGlobalVarDef;
import net.alaarc.vm.VmProgram;
import net.alaarc.vm.VmThreadDef;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dnpetrov
 */
public class ProgramCodeGenerator {
    private int lastGlobalVarId = 0; // this is not the last id, but rather the first unused id
    private Map<String, VmGlobalVarDef> globalVarsTable = new LinkedHashMap<>();
    private int lastThreadId = 0;
    private final List<VmThreadDef> vmThreadDefs = new ArrayList<>();

    private VmProgram vmProgram;

    VmGlobalVarDef getOrCreateVar(String name) {
        return globalVarsTable.computeIfAbsent(name, k -> new VmGlobalVarDef(lastGlobalVarId++, k));
    }

    int getNewThreadId() {
        return lastThreadId++;
    }

    void addChildThreadDef(VmThreadDef threadDef) {
        vmThreadDefs.add(threadDef);
    }

    public void run(AstProgram astProgram) {
        vmThreadDefs.clear();

        ThreadCodeGenerator threadCodeGenerator = new ThreadCodeGenerator(this);
        threadCodeGenerator.run(astProgram.getMainThreadBody());

        List<VmGlobalVarDef> globalVars = new ArrayList<>(globalVarsTable.values());
        VmThreadDef mainThreadDef = threadCodeGenerator.getThreadDef();
        vmThreadDefs.add(mainThreadDef);

        this.vmProgram = new VmProgram(globalVars, mainThreadDef, vmThreadDefs);
    }

    public VmProgram getVmProgram() {
        return vmProgram;
    }

    public List<VmThreadDef> getVmThreadDefs() {
        return vmThreadDefs;
    }
}

