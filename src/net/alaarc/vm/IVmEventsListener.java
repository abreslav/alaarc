package net.alaarc.vm;

/**
 * <p>
 *     Callback for VM events.
 * </p>
 * <p>
 *     <b>NB:</b> corresponding methods are executed from VM threads.
 * </p>
 *
 * @author dnpetrov
 */
public interface IVmEventsListener {
    void onObjectDisposed(long objectId);

    void onJavaException(Exception e);

    void onVmException(VmException e);

    void onThreadStart(int threadId);

    void onThreadFinish(int threadId);

    void onObjectDump(String dump);

    void onPostMessage(String message);
}
