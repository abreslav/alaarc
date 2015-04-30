package net.alaarc.vm.instructions;

import net.alaarc.ast.AstNode;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

/**
 * <code>( --> | thread sleeps for a random period of time )</code>
 *
 * @author dnpetrov
 */
public class SleepRand extends VmInstruction {
    public SleepRand(AstNode loc) {
        super(loc);
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitSleepRand(this);
    }

    @Override
    public String toString() {
        return "sleepRand " + " @" + getDebugInfo();
    }
}
