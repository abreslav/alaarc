package net.alaarc.interpreter;

import net.alaarc.vm.IVmEventsListener;

/**
 * @author dnpetrov
 */
public interface IInterpreterListener extends IVmEventsListener {
    default void onRunStarted(int i) {
        // do nothing
    }

    default void onRunFinished(int i) {
        // do nothing
    }

    default void onRunsStarted() {
        // do nothing
    }

    default void onRunsFinished() {
        // do nothing
    }
}
