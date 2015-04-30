package net.alaarc.vm;

import java.util.Objects;

/**
 * @author dnpetrov
 */
public class VmProgramInterpreter implements Runnable {
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

    public IVmEventsListener getVmEventsListener() {
        return vmEventsListener;
    }

    public IVmObjectFactory getObjectFactory() {
        return objectFactory;
    }

    @Override
    public void run() {
        spawnThread(program.getMainThread());
    }

    public void spawnThread(VmThreadDef vmThreadDef) {
        String threadName = "Alaarc-" + vmThreadDef.getThreadId();
        vmEventsListener.onThreadSpawned(threadName);
        VmThreadInterpreter threadInterpreter = new VmThreadInterpreter(this, vmThreadDef, threadName);
        Thread thread = new Thread(threadInterpreter);
        thread.setName(threadName);
        thread.start();
    }
}
