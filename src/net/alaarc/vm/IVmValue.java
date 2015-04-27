package net.alaarc.vm;

/**
 * @author dnpetrov
 */
public interface IVmValue {
    boolean isNull();
    IVmValue retain();
    void release();
    IVmValue weak();
    IVmValue getSlot(String slotName);
    IVmValue retainSlot(String slotName);
    IVmValue setSlot(String slotName, IVmValue newValue);
    String dump();
}
