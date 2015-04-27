package net.alaarc.vm;

import net.alaarc.vm.values.VmNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author dnpetrov
 */
public class VmGlobalVar {
    private final String name;
    private final AtomicReference<IVmValue> value = new AtomicReference<>(VmNull.NULL);
    private final AtomicInteger scopeCount = new AtomicInteger(1);

    public VmGlobalVar(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    public void retainVar() {
        scopeCount.incrementAndGet();
    }

    public void releaseVar() {
        while (true) {
            int oldScopeCount = scopeCount.get();
            if (scopeCount.compareAndSet(oldScopeCount, oldScopeCount-1)) {
                if (oldScopeCount == 1) {
                    weaken();
                }
                break;
            }
        }
    }

    public IVmValue getValue() {
        return value.get();
    }

    public IVmValue retainValue() {
        return value.get().retain();
    }

    public IVmValue getAndSetValue(IVmValue newValue) {
        return value.getAndSet(newValue);
    }

    private void weaken() {
        while (true) {
            IVmValue oldValue = value.get();
            if (value.compareAndSet(oldValue, oldValue.weak())) {
                oldValue.release();
                break;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        // Global variables are unique.
        return obj == this;
    }

    @Override
    public int hashCode() {
        // Two global variables with the same name should be the same instance.
        return name.hashCode();
    }
}
