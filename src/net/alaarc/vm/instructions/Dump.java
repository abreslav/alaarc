package net.alaarc.vm.instructions;

import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

/**
 * @author dnpetrov
 */
public class Dump extends VmInstruction {
    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitDump(this);
    }

    @Override
    public String toString() {
        return "DUMP";
    }
}
