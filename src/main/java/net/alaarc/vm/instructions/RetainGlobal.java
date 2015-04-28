package net.alaarc.vm.instructions;

import net.alaarc.vm.VmGlobalVar;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

import java.util.Objects;

/**
 * <code>( --> | 'var' is retained )</code>
 *
 * @author dnpetrov
 */
public class RetainGlobal extends VmInstruction {
    private final VmGlobalVar globalVar;

    public RetainGlobal(VmGlobalVar globalVar) {
        this.globalVar = Objects.requireNonNull(globalVar);
    }

    public VmGlobalVar getGlobalVar() {
        return globalVar;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitRetainGlobal(this);
    }

    @Override
    public String toString() {
        return "RETAIN_GLOBAL " + globalVar.getName();
    }
}
