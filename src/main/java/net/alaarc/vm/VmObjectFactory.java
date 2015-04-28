package net.alaarc.vm;

import net.alaarc.vm.values.VmObject;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dnpetrov
 */
public class VmObjectFactory implements IVmObjectFactory {
    private final AtomicLong objectsCounter = new AtomicLong(0);
    private final IVmEventsListener vmEventsListener;

    public VmObjectFactory(IVmEventsListener vmEventsListener) {
        this.vmEventsListener = vmEventsListener;
    }

    @Override
    public IVmValue createObject() {
        long id = objectsCounter.getAndIncrement();
        return new VmObject(vmEventsListener, id);
    }
}
