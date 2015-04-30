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
    void onJavaException(Exception e);
    void onVmException(VmException e);
    void onThreadSpawned(String threadName);
    void onThreadFinished(String threadName);
    void onObjectDump(String dump);
    void onPostMessage(String message);
    void onAssertionPassed(AssertRc instr);
    void onAssertionFailed(AssertRc instr);
}
