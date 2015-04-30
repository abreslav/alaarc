package net.alaarc.vm.instructions;

import net.alaarc.ast.AstNode;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

/**
 * <code>( a --> | dumps a )</code>
 *
 * @author dnpetrov
 */
public class Dump extends VmInstruction {
    public Dump(AstNode loc) {
        super(loc);
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitDump(this);
    }

    @Override
    public String toString() {
        return "dump @" + getDebugInfo();
    }
}
