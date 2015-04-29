package net.alaarc.vm.instructions;

import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

/**
 * <code>( --> [new object] )</code>
 *
 * @author dnpetrov
 */
public class CreateObject extends VmInstruction {

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitCreateObject(this);
    }

    @Override
    public String toString() {
        return "CREATE_OBJECT";
    }
}
