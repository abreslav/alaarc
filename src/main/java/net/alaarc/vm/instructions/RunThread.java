package net.alaarc.vm.instructions;

import net.alaarc.ast.AstNode;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

/**
 * <code>( --> | stars a new thread )<code>
 *
 * @author dnpetrov
 */
public class RunThread extends VmInstruction {
    private final int threadId;

    public RunThread(AstNode loc, int threadId) {
        super(loc);
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
        return "runThread " + threadId + " @" + getDebugInfo();
    }
}
