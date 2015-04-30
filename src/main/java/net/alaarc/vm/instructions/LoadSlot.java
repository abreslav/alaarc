package net.alaarc.vm.instructions;

import net.alaarc.ast.AstNode;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

import java.util.Objects;

/**
 * <code>( a --> [a.slot] )</code>
 *
 * @author dnpetrov
 */
public class LoadSlot extends VmInstruction {
    private final String slotName;

    public LoadSlot(AstNode loc, String slotName) {
        super(loc);
        this.slotName = Objects.requireNonNull(slotName);
    }

    public String getSlotName() {
        return slotName;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitLoadSlot(this);
    }

    @Override
    public String toString() {
        return "loadSlot " + slotName + " @" + getDebugInfo();
    }
}
