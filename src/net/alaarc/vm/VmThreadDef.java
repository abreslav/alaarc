package net.alaarc.vm;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author dnpetrov
 */
public class VmThreadDef {
    private final int threadId;
    private final List<VmInstruction> body;

    public VmThreadDef(int threadId, List<VmInstruction> body) {
        this.threadId = threadId;
        this.body = Objects.requireNonNull(body);
    }

    public int getThreadId() {
        return threadId;
    }

    public List<VmInstruction> getBody() {
        return Collections.unmodifiableList(body);
    }

    @Override
    public String toString() {
        return "thread #" + threadId;
    }
}
