package net.alaarc;

import net.alaarc.vm.VmException;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.instructions.AssertRc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author dnpetrov
 */
public class ComposedAlaarcListener implements IAlaarcListener {
    private final List<IAlaarcListener> listeners = new CopyOnWriteArrayList<>();

    public void addChildListener(IAlaarcListener listener) {
        if (listener instanceof ComposedAlaarcListener) {
            listeners.addAll(((ComposedAlaarcListener) listener).listeners);
        } else if (listener != null) {
            listeners.add(listener);
        }
    }

    public static IAlaarcListener compose(IAlaarcListener listener1, IAlaarcListener listener2) {
        if (listener1 == null) {
            return listener2;
        } else if (listener2 == null) {
            return listener1;
        } else {
            ComposedAlaarcListener result = new ComposedAlaarcListener();
            result.addChildListener(listener1);
            result.addChildListener(listener2);
            return result;
        }
    }

    @Override
    public void onProgramStarted() {
        listeners.forEach(IAlaarcListener::onProgramStarted);
    }

    @Override
    public void onProgramFinished() {
        listeners.forEach(IAlaarcListener::onProgramFinished);
    }

    @Override
    public void onObjectDisposed(long objectId) {
        for (IAlaarcListener listener : listeners) {
            listener.onObjectDisposed(objectId);
        }
    }

    @Override
    public void onJavaException(VmInstruction instr, Exception e) {
        for (IAlaarcListener listener : listeners) {
            listener.onJavaException(instr, e);
        }
    }

    @Override
    public void onVmException(VmInstruction instr, VmException e) {
        for (IAlaarcListener listener : listeners) {
            listener.onVmException(instr, e);
        }
    }

    @Override
    public void onThreadSpawned(VmInstruction instr, String threadName) {
        for (IAlaarcListener listener : listeners) {
            listener.onThreadSpawned(instr, threadName);
        }
    }

    @Override
    public void onThreadFinished(String threadName) {
        for (IAlaarcListener listener : listeners) {
            listener.onThreadFinished(threadName);
        }
    }

    @Override
    public void onObjectDump(VmInstruction instr, String dump) {
        for (IAlaarcListener listener : listeners) {
            listener.onObjectDump(instr, dump);
        }
    }

    @Override
    public void onPostMessage(VmInstruction instr, String message) {
        for (IAlaarcListener listener : listeners) {
            listener.onPostMessage(instr, message);
        }
    }

    @Override
    public void onAssertionPassed(AssertRc instr, long actualRc) {
        for (IAlaarcListener listener : listeners) {
            listener.onAssertionPassed(instr, actualRc);
        }
    }

    @Override
    public void onAssertionFailed(AssertRc instr, long actualRc) {
        for (IAlaarcListener listener : listeners) {
            listener.onAssertionFailed(instr, actualRc);
        }
    }

    @Override
    public void onHarnessStarted() {
        for (IAlaarcListener listener : listeners) {
            listener.onHarnessStarted();
        }
    }

    @Override
    public void onRunStarted(int i) {
        for (IAlaarcListener listener : listeners) {
            listener.onRunStarted(i);
        }
    }

    @Override
    public void onRunFinished(int i) {
        for (IAlaarcListener listener : listeners) {
            listener.onRunFinished(i);
        }
    }

    @Override
    public void onHarnessFinished() {
        for (IAlaarcListener listener : listeners) {
            listener.onHarnessFinished();
        }
    }
}
