package net.alaarc.vm;

import net.alaarc.vm.instructions.*;
import net.alaarc.vm.values.VmNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Random;

/**
 * @author dnpetrov
 */
public class VmThreadInterpreter implements Runnable {
    private final VmProgramInterpreter vmContext;
    private final VmThreadDef threadDef;
    private final String threadName;

    private final Random random = new Random();

    private final Deque<IVmValue> stack = new ArrayDeque<>(2);

    public VmThreadInterpreter(VmProgramInterpreter vmContext, VmThreadDef threadDef, String threadName) {
        this.vmContext = Objects.requireNonNull(vmContext);
        this.threadDef = Objects.requireNonNull(threadDef);
        this.threadName = Objects.requireNonNull(threadName);
    }

    @Override
    public void run() {
        // Control flow is linear, so we don't need program counter.
        try {
            for (VmInstruction instr : threadDef.getBody()) {
                try {
                    instr.accept(instructionInterpreter);
                } catch (VmException e) {
                    getListener().onVmException(instr, e);
                    break;
                } catch (Exception e) {
                    getListener().onJavaException(instr, e);
                    break;
                }
            }
        } finally{
            vmContext.threadFinished(threadName);
        }

    }

    private IVmEventsListener getListener() {
        return vmContext.getListener();
    }

    private IVmObjectFactory getObjectFactory() {
        return vmContext.getObjectFactory();
    }

    private IVmValue pop() {
        if (stack.isEmpty()) {
            throw new VmException("Internal error: stack underflow");
        }
        return stack.pop();
    }

    private void push(IVmValue value) {
        stack.push(value);
    }

    private final VmInstructionInterpreter instructionInterpreter = new VmInstructionInterpreter();

    private class VmInstructionInterpreter implements IVmInstructionVisitor {

        @Override
        public void visitLoadGlobal(LoadGlobal instr) {
            // ( --> [var] )
            VmGlobalVar var = vmContext.getGlobalVar(instr.getGlobalVar().getId());
            push(var.retainValue());
        }

        @Override
        public void visitLoadSlot(LoadSlot instr) {
            // ( x --> [x.slot] )
            IVmValue x = pop();
            IVmValue slotValue = x.retainSlot(instr.getSlotName());
            push(slotValue);
            x.release();
        }

        @Override
        public void visitStoreGlobal(StoreGlobal instr) {
            // ( nv --> | {var = nv} )
            IVmValue newValue = pop();
            VmGlobalVar var = vmContext.getGlobalVar(instr.getGlobalVar().getId());
            IVmValue oldValue = var.getAndSetValue(newValue);
            release(oldValue);
        }

        private void release(IVmValue oldValue) {
            if (oldValue != null) {
                oldValue.release();
            }
        }

        @Override
        public void visitStoreSlot(StoreSlot instr) {
            // ( nv x --> | {x.slot = nv } )
            IVmValue x = pop();
            IVmValue newValue = pop();
            IVmValue oldValue = x.setSlot(instr.getSlotName(), newValue);
            x.release();
            release(oldValue);
        }

        @Override
        public void visitStoreWeakGlobal(StoreWeakGlobal instr) {
            // ( x --> | {var ~= x} )
            IVmValue x = pop();
            VmGlobalVar var = vmContext.getGlobalVar(instr.getGlobalVar().getId());
            IVmValue oldValue = var.getAndSetValue(x.weak());
            x.release();
            release(oldValue);
        }

        @Override
        public void visitStoreWeakSlot(StoreWeakSlot instr) {
            // ( x y --> | {y.slot ~= x} )
            IVmValue x = pop();
            IVmValue y = pop();
            IVmValue oldValue = y.setSlot(instr.getSlotName(), x.weak());
            x.release();
            y.release();
            release(oldValue);
        }

        @Override
        public void visitSleep(Sleep instr) {
            // ( | {sleep} )
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                handleInterrupt();
            }
        }

        @Override
        public void visitSleepRand(SleepRand instr) {
            // ( | {sleepr} )
            try {
                Thread.sleep(random.nextInt(90) + 10);
            } catch (InterruptedException e) {
                handleInterrupt();
            }
        }

        @Override
        public void visitDump(Dump instr) {
            // ( x --> | {dump x} )
            IVmValue x = pop();
            getListener().onObjectDump(instr, x.dump());
            x.release();
        }

        @Override
        public void visitRunThread(RunThread instr) {
            // ( --> | {thread {...} } )
            VmThreadDef threadDef = vmContext.getThreadDef(instr.getThreadId());
            vmContext.spawnThread(instr, threadDef);
        }

        @Override
        public void visitCreateObject(CreateObject instr) {
            // ( --> [object] )
            IVmValue x = getObjectFactory().createObject();
            push(x.retain());
        }

        @Override
        public void visitRetainGlobal(RetainGlobal instr) {
            // ( --> | {var.retainVar()} )
            VmGlobalVar var = vmContext.getGlobalVar(instr.getGlobalVar().getId());
            var.retainVar();
        }

        @Override
        public void visitReleaseGlobal(ReleaseGlobal instr) {
            // ( --> | {var.releaseVar()} )
            VmGlobalVar var = vmContext.getGlobalVar(instr.getGlobalVar().getId());
            var.releaseVar();
        }

        @Override
        public void visitPostMessage(PostMessage instr) {
            // ( --> | {log message} )
            getListener().onPostMessage(instr, instr.getMessage());
        }

        @Override
        public void visitAssertRc(AssertRc instr) {
            // ( x --> | {assertion on refCount(x)} )
            IVmValue x = pop();
            long refCount = x.getRefCount();
            long num = instr.getNumber();
            if (instr.getComparisonOperator().compare(refCount, num)) {
                getListener().onAssertionPassed(instr);
            } else {
                getListener().onAssertionFailed(instr);
            }
            x.release();
        }

        @Override
        public void visitPushNull(PushNull instr) {
            // ( --> [null] )
            push(VmNull.NULL);
        }
    }

    private void handleInterrupt() {
        // does nothing
    }
}
