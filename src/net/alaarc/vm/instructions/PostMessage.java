package net.alaarc.vm.instructions;

import net.alaarc.vm.IVmInstructionVisitor;
import net.alaarc.vm.VmInstruction;

/**
 * @author dnpetrov
 */
public class PostMessage extends VmInstruction {
    private final String message;

    public PostMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitPostMessage(this);
    }

    @Override
    public String toString() {
        return "POST_MESSAGE \"" + message + "\"";
    }
}
