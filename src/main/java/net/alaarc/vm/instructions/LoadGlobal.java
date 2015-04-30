package net.alaarc.vm.instructions;

import net.alaarc.vm.VmGlobalVar;
import net.alaarc.vm.VmGlobalVarDef;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

import java.util.Objects;

/**
 * <code>( --> [var] )</code>
 *
 * @author dnpetrov
 */
public class LoadGlobal extends VmInstruction {
    private final VmGlobalVarDef globalVar;

    public LoadGlobal(VmGlobalVarDef globalVar) {
        this.globalVar = Objects.requireNonNull(globalVar);
    }

    public VmGlobalVarDef getGlobalVar() {
        return globalVar;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitLoadGlobal(this);
    }

    @Override
    public String toString() {
        return "loadGlobal " + globalVar.getName();
    }
}
