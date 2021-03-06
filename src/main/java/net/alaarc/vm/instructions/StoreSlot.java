package net.alaarc.vm.instructions;

import net.alaarc.ast.AstNode;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

import java.util.Objects;

/**
 * <code>( a b --> | [b.slot = a] )</code>
 *
 * @author dnpetrov
 */
public class StoreSlot extends VmInstruction {
    private final String slotName;

    public StoreSlot(AstNode loc, String slotName) {
        super(loc);
        this.slotName = Objects.requireNonNull(slotName);
    }

    public String getSlotName() {
        return slotName;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitStoreSlot(this);
    }

    @Override
    public String toString() {
        return "storeSlot " + slotName + " @" + getDebugInfo();
    }
}
