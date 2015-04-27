package net.alaarc.vm.values;

import net.alaarc.vm.IVmValue;

/**
 * @author dnpetrov
 */
public class VmWeakRef implements IVmValue {
    private volatile IVmValue object;

    public VmWeakRef(IVmValue object) {
        this.object = object;
    }

    public void dispose() {
        object = VmNull.NULL;
    }

    @Override
    public boolean isNull() {
        return object.isNull();
    }

    @Override
    public IVmValue retain() {
        return this;
    }

    @Override
    public void release() {
        // does nothing
    }

    @Override
    public IVmValue weak() {
        return this;
    }

    @Override
    public IVmValue getSlot(String slotName) {
        return object.getSlot(slotName);
    }

    @Override
    public IVmValue retainSlot(String slotName) {
        return object.retainSlot(slotName);
    }

    @Override
    public IVmValue setSlot(String slotName, IVmValue newValue) {
        return object.setSlot(slotName, newValue);
    }

    @Override
    public String dump() {
        return "~" + object.dump();
    }

    @Override
    public String toString() {
        return "~" + object.toString();
    }
}
