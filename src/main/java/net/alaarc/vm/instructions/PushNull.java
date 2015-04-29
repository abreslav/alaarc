package net.alaarc.vm.instructions;

import net.alaarc.vm.IVmInstructionVisitor;
import net.alaarc.vm.VmInstruction;

/**
 * @author dnpetrov
 */
public class PushNull extends VmInstruction {
    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitPushNull(this);
    }

    @Override
    public String toString() {
        return "pushNull";
    }
}
