package net.alaarc.vm.instructions;

import net.alaarc.ast.AstNode;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

/**
 * <code>( --> [new object] )</code>
 *
 * @author dnpetrov
 */
public class CreateObject extends VmInstruction {
    public CreateObject(AstNode loc) {
        super(loc);
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitCreateObject(this);
    }

    @Override
    public String toString() {
        return "createObject @" + getDebugInfo();
    }
}
