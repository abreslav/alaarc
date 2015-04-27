package net.alaarc.vm;

/**
 * @author dnpetrov
 */
public interface IVmContext {
    IVmEventsListener getVmEventsListener();
    IVmObjectFactory getObjectFactory();
}
