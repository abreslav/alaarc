package net.alaarc.vm.instructions;

import net.alaarc.ast.AstNode;
import net.alaarc.vm.IVmInstructionVisitor;
import net.alaarc.vm.VmInstruction;

/**
 * @author dnpetrov
 */
public class PushNull extends VmInstruction {
    public PushNull(AstNode loc) {
        super(loc);
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitPushNull(this);
    }

    @Override
    public String toString() {
        return "pushNull " + " @" + getDebugInfo();
    }
}
