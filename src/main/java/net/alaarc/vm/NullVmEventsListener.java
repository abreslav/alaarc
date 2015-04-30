package net.alaarc.vm;

import net.alaarc.vm.instructions.AssertRc;

/**
 * @author dnpetrov
 */
public class NullVmEventsListener implements IVmEventsListener {
    @Override
    public void onProgramStarted() {
    }

    @Override
    public void onProgramFinished() {
    }

    @Override
    public void onObjectDisposed(long objectId) {
    }

    @Override
    public void onJavaException(VmInstruction instr, Exception e) {
    }

    @Override
    public void onVmException(VmInstruction instr, VmException e) {
    }

    @Override
    public void onThreadSpawned(VmInstruction instr, String threadName) {
    }

    @Override
    public void onThreadFinished(String threadName) {
    }

    @Override
    public void onObjectDump(VmInstruction instr, String dump) {
    }

    @Override
    public void onPostMessage(VmInstruction instr, String message) {
    }

    @Override
    public void onAssertionPassed(AssertRc instr) {
    }

    @Override
    public void onAssertionFailed(AssertRc instr) {
    }
}
