package net.alaarc.vm.instructions;

import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

import java.util.Objects;

/**
 * @author dnpetrov
 */
public class StoreSlot extends VmInstruction {
    private final String slotName;

    public StoreSlot(String slotName) {
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
        return "STORE_SLOT " + slotName;
    }
}
