package net.alaarc.vm;

import net.alaarc.vm.instructions.AssertRc;

/**
 * <p>Callback for VM events.</p>
 * <p><b>NB:</b> corresponding methods are executed from VM threads.</p>
 *
 * @author dnpetrov
 */
public interface IVmEventsListener {
    void onProgramStarted();
    void onProgramFinished();
    void onObjectDisposed(long objectId);
    void onJavaException(VmInstruction instr, Exception e);
    void onVmException(VmInstruction instr, VmException e);
    void onThreadSpawned(VmInstruction instr, String threadName);
    void onThreadFinished(String threadName);
    void onObjectDump(VmInstruction instr, String dump);
    void onPostMessage(VmInstruction instr, String message);
    void onAssertionPassed(AssertRc instr);
    void onAssertionFailed(AssertRc instr);
}
