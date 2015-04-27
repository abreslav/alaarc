package net.alaarc.vm.instructions;

import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

/**
 * @author dnpetrov
 */
public class SleepRand extends VmInstruction {
    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitSleepRand(this);
    }
}
