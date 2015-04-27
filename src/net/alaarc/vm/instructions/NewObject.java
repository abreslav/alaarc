package net.alaarc.vm.instructions;

import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

/**
 * @author dnpetrov
 */
public class NewObject extends VmInstruction {

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitNewObject(this);
    }

    @Override
    public String toString() {
        return "NEW_OBJECT";
    }
}
