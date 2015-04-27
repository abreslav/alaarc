package net.alaarc.vm.instructions;

import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

/**
 * @author dnpetrov
 */
public class Sleep extends VmInstruction {

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitSleep(this);
    }

    @Override
    public String toString() {
        return "SLEEP";
    }
}
