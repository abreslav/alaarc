package net.alaarc;

import net.alaarc.vm.VmException;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.instructions.AssertRc;

/**
 * @author dnpetrov
 */
public class DefaultAlaarcListener implements IAlaarcListener {
    @Override
    public void onProgramStarted() {
    }

    @Override
    public void onProgramFinished() {
    }

    @Override
    public void onObjectCreated(long objectId) {
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
    public void onAssertionPassed(AssertRc instr, long actualRc) {
    }

    @Override
    public void onAssertionFailed(AssertRc instr, long actualRc) {
    }

    @Override
    public void onHarnessStarted() {
    }

    @Override
    public void onRunStarted(int i) {
    }

    @Override
    public void onRunFinished(int i) {
    }

    @Override
    public void onHarnessFinished() {
    }
}
