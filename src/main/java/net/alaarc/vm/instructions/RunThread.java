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
    private final VmThreadDef threadDef;

    public RunThread(VmThreadDef threadDef) {
        this.threadDef = Objects.requireNonNull(threadDef);
    }

    public VmThreadDef getThreadDef() {
        return threadDef;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitRunThread(this);
    }

    @Override
    public String toString() {
        return "runThread " + threadDef.getThreadId();
    }
}
