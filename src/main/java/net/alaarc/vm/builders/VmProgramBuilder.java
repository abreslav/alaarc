package net.alaarc.vm.builders;

import net.alaarc.vm.VmGlobalVar;
import net.alaarc.vm.VmProgram;
import net.alaarc.vm.VmThreadDef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author dnpetrov
 */
public class VmProgramBuilder {
    private final Map<String, VmGlobalVar> globalVars = new HashMap<>();
    private int firstThreadId = 1;
    private VmThreadDef mainThreadDef;
    private List<VmThreadDef> vmThreadDefs = new ArrayList<>();

    public VmGlobalVar global(String name) {
        return globalVars.computeIfAbsent(name, VmGlobalVar::new);
    }

    public void setMainThreadDef(VmThreadDef mainThreadDef) {
        this.mainThreadDef = mainThreadDef;
    }

    public VmThreadDef getMainThreadDef() {
        return mainThreadDef;
    }

    void addThreadDef(VmThreadDef threadDef) {
        vmThreadDefs.add(threadDef);
    }

    public VmProgram buildProgram() {
        List<VmGlobalVar> globalVarList = new ArrayList<>(globalVars.values());
        return new VmProgram(globalVarList, mainThreadDef, vmThreadDefs);
    }

    public static VmProgram withMainThread(Consumer<VmThreadBuilder> mainThread) {
        VmProgramBuilder programBuilder = new VmProgramBuilder();
        VmThreadBuilder mainThreadBuilder = new VmThreadBuilder(0, programBuilder);
        mainThread.accept(mainThreadBuilder);
        VmThreadDef mainThreadDef = mainThreadBuilder.buildThreadDef();
        programBuilder.setMainThreadDef(mainThreadDef);
        programBuilder.addThreadDef(mainThreadDef);
        return programBuilder.buildProgram();
    }

    public int nextThreadId() {
        return firstThreadId++;
    }

}
