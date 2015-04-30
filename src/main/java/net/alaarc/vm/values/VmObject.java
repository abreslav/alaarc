package net.alaarc.vm.values;

import net.alaarc.vm.*;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author dnpetrov
 */
public class VmObject implements IVmValue {
    private final IVmEventsListener vmEventsListener;

    private final long objectId;

    private final AtomicLong refCount = new AtomicLong(0);

    private final VmWeakRef weakRef = new VmWeakRef(this);

    private final AtomicReference<ConcurrentHashMap<String, IVmValue>> slots =
            new AtomicReference<>(new ConcurrentHashMap<>());

    private final MultiReadMultiWriteMutex rwMutex = new MultiReadMultiWriteMutex();

    public VmObject(IVmEventsListener vm, long objectId) {
        this.vmEventsListener = Objects.requireNonNull(vm);
        this.objectId = objectId;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    private boolean isDisposed() {
        return refCount.get() <= 0;
    }

    @Override
    public IVmValue retain() {
        refCount.incrementAndGet();
        return this;
    }

    @Override
    public void release() {
        if (isDisposed()) return;
        rwMutex.withWriteLock(
                () -> {
                    while (true) {
                        long oldRefCount = refCount.get();
                        if (oldRefCount <= 0) {
                            // Someone got ahead of us and disposed this object.
                            return;
                        }
                        if (refCount.compareAndSet(oldRefCount, oldRefCount - 1)) {
                            if (oldRefCount == 1) {
                                dispose();
                                vmEventsListener.onObjectDisposed(objectId);
                            }
                            return;
                        }
                    }
                }
        );
    }

    private void dispose() {
        weakRef.dispose();
        ConcurrentHashMap<String, IVmValue> oldSlots = slots.getAndSet(null);
        // Release slot values
        oldSlots.values().forEach(IVmValue::release);
    }

    @Override
    public IVmValue weak() {
        return weakRef;
    }

    @Override
    public IVmValue getSlot(String slotName) {
        requireAlive();
        IVmValue oldValue = slots.get().computeIfAbsent(slotName,
                key -> {
                    throw new VmException("getSlot(" + key + "): slot is undefined");
                }
        );
        if (oldValue == null) {
            oldValue = VmNull.NULL;
        }
        return oldValue;
    }

    private void requireAlive() {
        ConcurrentHashMap<String, IVmValue> currSlots = slots.get();
        if (currSlots == null) {
            throw new RuntimeException("Internal error: " + toString() + " has been incorrectly disposed.");
        }
    }

    @Override
    public IVmValue setSlot(String slotName, IVmValue newValue) {
        requireAlive();
        return rwMutex.withWriteLock(
                () -> slots.get().put(slotName, newValue)
        );
    }

    @Override
    public String dump() {
        return rwMutex.withReadLock(
                () -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("#");
                    sb.append(objectId);
                    ConcurrentHashMap<String, IVmValue> currSlots = this.slots.get();
                    if (currSlots == null) {
                        sb.append(" <disposed>");
                    } else {
                        long currRefCount = this.refCount.get();
                        sb.append(" rc=");
                        sb.append(currRefCount);
                        sb.append(" { ");
                        for (Map.Entry<String, IVmValue> entry : currSlots.entrySet()) {
                            String slotName = entry.getKey();
                            IVmValue slotValue = entry.getValue();
                            sb.append(slotName);
                            sb.append("=");
                            sb.append(slotValue.toString());
                            sb.append("; ");
                        }
                        sb.append("}");
                    }

                    return sb.toString();
                }
        );
    }

    @Override
    public long getRefCount() {
        return refCount.get();
    }

    @Override
    public String toString() {
        return "#" + objectId;
    }

}
