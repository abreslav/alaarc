package net.alaarc;

import net.alaarc.vm.VmException;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.instructions.AssertRc;

/**
 * <p>Callback for VM events.</p>
 * <p><b>NB:</b> corresponding methods are executed from VM threads.</p>
 *
 * @author dnpetrov
 */
public interface IAlaarcEventsListener {
    void onProgramStarted();
    void onProgramFinished();
    void onObjectDisposed(long objectId);
    void onJavaException(VmInstruction instr, Exception e);
    void onVmException(VmInstruction instr, VmException e);
    void onThreadSpawned(VmInstruction instr, String threadName);
    void onThreadFinished(String threadName);
    void onObjectDump(VmInstruction instr, String dump);
    void onPostMessage(VmInstruction instr, String message);
    void onAssertionPassed(AssertRc instr, long actualRc);
    void onAssertionFailed(AssertRc instr, long actualRc);

    default void onRunStarted(int i) {
        // do nothing
    }

    default void onRunFinished(int i) {
        // do nothing
    }

    default void onHarnessStarted() {
        // do nothing
    }

    default void onHarnessFinished() {
        // do nothing
    }
}
