package net.alaarc.vm.instructions;

import net.alaarc.vm.VmGlobalVar;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

import java.util.Objects;

/**
 * @author dnpetrov
 */
public class StoreGlobal extends VmInstruction {
    private final VmGlobalVar globalVar;

    public StoreGlobal(VmGlobalVar globalVar) {
        this.globalVar = Objects.requireNonNull(globalVar);
    }

    public VmGlobalVar getGlobalVar() {
        return globalVar;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitStoreGlobal(this);
    }

    @Override
    public String toString() {
        return "STORE_GLOBAL " + globalVar.getName();
    }
}
