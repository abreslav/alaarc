package net.alaarc.vm;

import net.alaarc.IAlaarcEventsListener;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author dnpetrov
 */
public class VmProgramInterpreter implements Runnable {
    private final IAlaarcEventsListener listener;
    private final IVmObjectFactory objectFactory;
    private final VmProgram program;

    private final VmThreadDef[] vmThreadDefs;
    private final VmGlobalVar[] globalVars;

    private final AtomicInteger threadsCount = new AtomicInteger(0);

    public VmProgramInterpreter(IAlaarcEventsListener listener, IVmObjectFactory objectFactory, VmProgram program) {
        this.listener = Objects.requireNonNull(listener);
        this.objectFactory = Objects.requireNonNull(objectFactory);
        this.program = Objects.requireNonNull(program);

        this.globalVars = createGlobalVars(program);
        this.vmThreadDefs = createThreadDefs(program);
    }

    public VmProgramInterpreter(IAlaarcEventsListener listener, VmProgram program) {
        this(listener, new VmObjectFactory(listener), program);
    }

    private static VmGlobalVar[] createGlobalVars(VmProgram vmProgram) {
        List<VmGlobalVarDef> globalVarDefs = vmProgram.getGlobalVarDefs();
        VmGlobalVar[] vmGlobalVars = new VmGlobalVar[globalVarDefs.size()];
        for (VmGlobalVarDef gvd : globalVarDefs) {
            vmGlobalVars[gvd.getId()] = new VmGlobalVar(gvd.getName());
        }
        return vmGlobalVars;
    }

    private static VmThreadDef[] createThreadDefs(VmProgram program) {
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

    public IAlaarcEventsListener getListener() {
        return listener;
    }

    public IVmObjectFactory getObjectFactory() {
        return objectFactory;
    }

    private final Object executionFinished = new Object();

    @Override
    public void run() {
        listener.onProgramStarted();
        synchronized (executionFinished) {
            spawnThread(null, program.getMainThread());
            try {
                executionFinished.wait();
            } catch (InterruptedException e) {
                // swallow it
            }
        }

    }

    public void spawnThread(VmInstruction instr, VmThreadDef vmThreadDef) {
        String threadName = "Alaarc-" + vmThreadDef.getThreadId();
        threadsCount.incrementAndGet();
        listener.onThreadSpawned(instr, threadName);
        VmThreadInterpreter threadInterpreter = new VmThreadInterpreter(this, vmThreadDef, threadName);
        Thread thread = new Thread(threadInterpreter);
        thread.setName(threadName);
        thread.start();
    }

    public void threadFinished(String threadName) {
        listener.onThreadFinished(threadName);
        int numThreads = threadsCount.decrementAndGet();
        if (numThreads == 0) {
            listener.onProgramFinished();
            synchronized (executionFinished) {
                executionFinished.notify();
            }
        }
    }
}
