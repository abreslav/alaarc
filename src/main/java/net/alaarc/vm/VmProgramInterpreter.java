package net.alaarc.vm;

import java.util.List;
import java.util.Objects;

/**
 * @author dnpetrov
 */
public class VmProgramInterpreter implements Runnable {
    private final IVmEventsListener vmEventsListener;
    private final IVmObjectFactory objectFactory;
    private final VmProgram program;

    private final VmThreadDef[] vmThreadDefs;
    private final VmGlobalVar[] globalVars;

    public VmProgramInterpreter(IVmEventsListener vmEventsListener, IVmObjectFactory objectFactory, VmProgram program) {
        this.vmEventsListener = Objects.requireNonNull(vmEventsListener);
        this.objectFactory = Objects.requireNonNull(objectFactory);
        this.program = Objects.requireNonNull(program);

        this.globalVars = createGlobalVars(program);
        this.vmThreadDefs = createThreadDefs(program);
    }

    private VmGlobalVar[] createGlobalVars(VmProgram vmProgram) {
        List<VmGlobalVarDef> globalVarDefs = vmProgram.getGlobalVarDefs();
        VmGlobalVar[] vmGlobalVars = new VmGlobalVar[globalVarDefs.size()];
        for (VmGlobalVarDef gvd : globalVarDefs) {
            vmGlobalVars[gvd.getId()] = new VmGlobalVar(gvd.getName());
        }
        return vmGlobalVars;
    }

    private VmThreadDef[] createThreadDefs(VmProgram program) {
        List<VmThreadDef> programThreadDefs = program.getVmThreadDefs();
        VmThreadDef[] threadDefs = new VmThreadDef[programThreadDefs.size()];
        for (VmThreadDef ptd : programThreadDefs) {
            threadDefs[ptd.getThreadId()] = ptd;
        }
        return threadDefs;
    }

    public VmGlobalVar getGlobalVar(int id) {
        return globalVars[id];
    }

    public VmThreadDef getThreadDef(int id) {
        return vmThreadDefs[id];
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
