package net.alaarc.vm.instructions;

import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

/**
 * <code>( --> | thread sleeps for a random period of time )</code>
 *
 * @author dnpetrov
 */
public class SleepRand extends VmInstruction {
    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitSleepRand(this);
    }
}
