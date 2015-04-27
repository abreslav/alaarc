package net.alaarc.vm.instructions;

import net.alaarc.vm.VmGlobalVar;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

import java.util.Objects;

/**
 * @author dnpetrov
 */
public class ReleaseGlobal extends VmInstruction {
    private final VmGlobalVar globalVar;

    public ReleaseGlobal(VmGlobalVar globalVar) {
        this.globalVar = Objects.requireNonNull(globalVar);
    }

    public VmGlobalVar getGlobalVar() {
        return globalVar;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitReleaseGlobal(this);
    }

    @Override
    public String toString() {
        return "RELEASE_GLOBAL " + globalVar.getName();
    }
}
