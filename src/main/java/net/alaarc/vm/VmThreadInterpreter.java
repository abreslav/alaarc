package net.alaarc.vm;

import net.alaarc.vm.instructions.*;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Random;

/**
 * @author dnpetrov
 */
public class VmThreadInterpreter implements Runnable {
    private final IVmContext vmContext;

    private final VmThreadDef threadDef;

    private final Random random = new Random();

    private final Deque<IVmValue> stack = new ArrayDeque<>(2);

    public VmThreadInterpreter(IVmContext vmContext, VmThreadDef threadDef) {
        this.vmContext = Objects.requireNonNull(vmContext);
        this.threadDef = Objects.requireNonNull(threadDef);
    }

    @Override
    public void run() {
        Thread.currentThread().setName("Alaarc-Thread #" + threadDef.getThreadId());

        getVmEventsListener().onThreadStart(threadDef.getThreadId());

        // Control flow is linear, so we don't need program counter.
        for (VmInstruction instr : threadDef.getBody()) {
            try {
                instr.accept(instructionInterpreter);
            } catch (VmException e) {
                getVmEventsListener().onVmException(e);
                break;
            } catch (Exception e) {
                getVmEventsListener().onJavaException(e);
                break;
            }
        }

        getVmEventsListener().onThreadFinish(threadDef.getThreadId());
    }

    private IVmEventsListener getVmEventsListener() {
        return vmContext.getVmEventsListener();
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
            push(instr.getGlobalVar().retainValue());
        }

        @Override
        public void visitLoadSlot(LoadSlot instr) {
            IVmValue x = pop();
            IVmValue slotValue = x.retainSlot(instr.getSlotName());
            push(slotValue);
            x.release();
        }

        @Override
        public void visitStoreGlobal(StoreGlobal instr) {
            IVmValue x = pop();
            IVmValue oldValue = instr.getGlobalVar().getAndSetValue(x);
            oldValue.release();
        }

        @Override
        public void visitStoreSlot(StoreSlot instr) {
            IVmValue x = pop();
            IVmValue y = pop();
            IVmValue oldValue = y.setSlot(instr.getSlotName(), x);
            y.release();
            oldValue.release();
        }

        @Override
        public void visitStoreWeakGlobal(StoreWeakGlobal instr) {
            IVmValue x = pop();
            IVmValue oldValue = instr.getGlobalVar().getAndSetValue(x.weak());
            x.release();
            oldValue.release();
        }

        @Override
        public void visitStoreWeakSlot(StoreWeakSlot instr) {
            IVmValue x = pop();
            IVmValue y = pop();
            IVmValue oldValue = y.setSlot(instr.getSlotName(), x.weak());
            x.release();
            y.release();
            oldValue.release();
        }

        @Override
        public void visitSleep(Sleep instr) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                handleInterrupt();
            }
        }

        @Override
        public void visitSleepRand(SleepRand instr) {
            try {
                Thread.sleep(random.nextInt(90) + 10);
            } catch (InterruptedException e) {
                handleInterrupt();
            }
        }

        @Override
        public void visitDump(Dump instr) {
            IVmValue x = pop();
            getVmEventsListener().onObjectDump(x.dump());
            x.release();
        }

        @Override
        public void visitRunThread(RunThread instr) {
            VmThreadInterpreter child = new VmThreadInterpreter(vmContext, instr.getThreadDef());
            new Thread(child).start();
        }

        @Override
        public void visitNewObject(NewObject instr) {
            IVmValue x = getObjectFactory().createObject();
            push(x.retain());
        }

        @Override
        public void visitRetainGlobal(RetainGlobal instr) {
            instr.getGlobalVar().retainVar();
        }

        @Override
        public void visitReleaseGlobal(ReleaseGlobal instr) {
            instr.getGlobalVar().releaseVar();
        }

        @Override
        public void visitPostMessage(PostMessage instr) {
            getVmEventsListener().onPostMessage(instr.getMessage());
        }

        @Override
        public void visitAssertRc(AssertRc instr) {
            IVmValue x = pop();
            long refCount = x.getRefCount();
            long num = instr.getNumber();
            if (instr.getComparisonOp().compare(refCount, num)) {
                getVmEventsListener().onAssertionPassed(instr);
            } else {
                getVmEventsListener().onAssertionFailed(instr);
            }

            x.release();
        }


    }



    private void handleInterrupt() {
        // does nothing
    }
}
