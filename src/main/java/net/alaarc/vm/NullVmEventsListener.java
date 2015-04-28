package net.alaarc.vm;

import net.alaarc.vm.instructions.AssertRc;

/**
 * @author dnpetrov
 */
public class NullVmEventsListener implements IVmEventsListener {
    @Override
    public void onObjectDisposed(long objectId) {
    }

    @Override
    public void onJavaException(Exception e) {
    }

    @Override
    public void onVmException(VmException e) {
    }

    @Override
    public void onThreadStart(int threadId) {
    }

    @Override
    public void onThreadFinish(int threadId) {
    }

    @Override
    public void onObjectDump(String dump) {
    }

    @Override
    public void onPostMessage(String message) {
    }

    @Override
    public void onAssertionPassed(AssertRc instr) {
    }

    @Override
    public void onAssertionFailed(AssertRc instr) {
    }
}
