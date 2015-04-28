package net.alaarc.vm.values;

import net.alaarc.vm.IVmValue;
import net.alaarc.vm.VmException;

/**
 * @author dnpetrov
 */
public class VmNull implements IVmValue {
    public static final VmNull NULL = new VmNull();

    private VmNull() {
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public IVmValue retain() {
        return this;
    }

    @Override
    public void release() {
        // do nothing
    }

    @Override
    public IVmValue weak() {
        return this;
    }

    @Override
    public IVmValue getSlot(String slotName) {
        throw new VmException("getSlot(" + slotName + "): object is null");
    }

    @Override
    public IVmValue retainSlot(String slotName) {
        throw new VmException("retainSlot(" + slotName + "): object is null");
    }

    @Override
    public IVmValue setSlot(String slotName, IVmValue newValue) {
        throw new VmException("setSlot(" + slotName + "): object is null");
    }

    @Override
    public String dump() {
        return "NULL";
    }

    @Override
    public long getRefCount() {
        return -1;
    }

    @Override
    public String toString() {
        return "NULL";
    }
}
