package net.alaarc.vm;

import net.alaarc.IAlaarcListener;
import net.alaarc.vm.values.VmObject;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dnpetrov
 */
public class VmObjectFactory implements IVmObjectFactory {
    private final AtomicLong objectsCounter = new AtomicLong(0);
    private final IAlaarcListener vmEventsListener;

    public VmObjectFactory(IAlaarcListener vmEventsListener) {
        this.vmEventsListener = vmEventsListener;
    }

    @Override
    public IVmValue createObject() {
        long id = objectsCounter.getAndIncrement();
        vmEventsListener.onObjectCreated(id);
        return new VmObject(vmEventsListener, id);
    }
}
