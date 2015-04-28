package net.alaarc.vm;

/**
 * <p>
 *     Value operated by VM. Possible values are:
 * </p>
 * <ul>
 *     <li>object (owning) reference</li>
 *     <li>weak (non-owning) object reference</li>
 *     <li>null</li>
 * </ul>
 * <p>
 *     Objects contain slots.
 *     Slots are introduced by the first assignment (<code>a.b = ...</code>).
 *     When an object is disposed, slots are released.
 * </p>
 * <p>
 *     Objects are reference counted.
 *     When an object is disposed, its weak reference(s) become 'null'.
 * </p>
 *
 * @author dnpetrov
 */
public interface IVmValue {
    /**
     * Checks if this object is null.
     *
     * @return true is this is a null value
     */
    boolean isNull();

    /**
     * Retains an object (increments its reference counter).
     *
     * @return this object
     */
    IVmValue retain();

    /**
     * Releases an object (decrements its reference counter).
     */
    void release();

    /**
     * <p>Obtains a weak reference for an object.</p>
     * <p>Weak reference for a weak reference 'x' is 'x'.</p>
     * <p>Weak reference for null is null.</p>
     *
     * @return weak reference for this object
     */
    IVmValue weak();

    /**
     * Get slot value.
     *
     * @param slotName name of the corresponding slot
     *
     * @return slot value
     */
    IVmValue getSlot(String slotName);

    /**
     * Retains a slot value. Equivalent to <code>getSlot(slotName).retain()</code>.
     *
     * @param slotName name of the corresponding slot
     *
     * @return slot value
     */
    default IVmValue retainSlot(String slotName) {
        return getSlot(slotName).retain();
    }

    /**
     * Sets a slot value.
     *
     * @param slotName name of the corresponding slot
     * @param newValue new value
     *
     * @return previous value of the corresponding slot
     */
    IVmValue setSlot(String slotName, IVmValue newValue);

    /**
     * Dumps this value.
     *
     * @return value dump represented as a String
     */
    String dump();

    /**
     * Get reference count. Used for testing assertions only.
     *
     * @return reference count for an object, -1 for null
     */
    long getRefCount();
}
