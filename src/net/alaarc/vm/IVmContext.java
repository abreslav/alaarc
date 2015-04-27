package net.alaarc.vm;

/**
 * Top-level VM context.
 *
 * @author dnpetrov
 */
public interface IVmContext {
    IVmEventsListener getVmEventsListener();
    IVmObjectFactory getObjectFactory();
}
