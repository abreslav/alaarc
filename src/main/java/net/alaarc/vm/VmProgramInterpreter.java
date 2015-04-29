package net.alaarc.vm;

import java.util.Objects;

/**
 * @author dnpetrov
 */
public class VmProgramInterpreter implements IVmContext, Runnable {
    private final IVmEventsListener vmEventsListener;
    private final IVmObjectFactory objectFactory;
    private final VmProgram program;

    public VmProgramInterpreter(IVmEventsListener vmEventsListener, IVmObjectFactory objectFactory, VmProgram program) {
        this.vmEventsListener = Objects.requireNonNull(vmEventsListener);
        this.objectFactory = Objects.requireNonNull(objectFactory);
        this.program = Objects.requireNonNull(program);
    }

    public VmProgramInterpreter(IVmEventsListener vmEventsListener, VmProgram program) {
        this(vmEventsListener, new VmObjectFactory(vmEventsListener), program);
    }

    @Override
    public IVmEventsListener getVmEventsListener() {
        return vmEventsListener;
    }

    @Override
    public IVmObjectFactory getObjectFactory() {
        return objectFactory;
    }

    @Override
    public void run() {
        vmEventsListener.onThreadSpawned(program.getMainThread().getThreadId());
        new Thread(new VmThreadInterpreter(this, program.getMainThread())).start();
    }
}
