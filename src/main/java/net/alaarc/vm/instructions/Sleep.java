package net.alaarc.vm.instructions;

import net.alaarc.ast.AstNode;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

/**
 * <code>( --> | thread sleeps for a fixed period of time )</code>
 *
 * @author dnpetrov
 */
public class Sleep extends VmInstruction {

    public Sleep(AstNode loc) {
        super(loc);
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitSleep(this);
    }

    @Override
    public String toString() {
        return "sleep " + " @" + getDebugInfo();
    }
}
