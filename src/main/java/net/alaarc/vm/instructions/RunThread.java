package net.alaarc.vm.instructions;

import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;
import net.alaarc.vm.VmThreadDef;

import java.util.Objects;

/**
 * <code>( --> | stars a new thread )<code>
 *
 * @author dnpetrov
 */
public class RunThread extends VmInstruction {
    private final int threadId;

    public RunThread(int threadId) {
        this.threadId = threadId;
    }

    public int getThreadId() {
        return threadId;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitRunThread(this);
    }

    @Override
    public String toString() {
        return "runThread " + threadId;
    }
}
