package net.alaarc.vm.instructions;

import net.alaarc.ast.AstNode;
import net.alaarc.vm.IVmInstructionVisitor;
import net.alaarc.vm.VmInstruction;

/**
 * <code>( --> | posts message to VM logger )</code>
 *
 * @author dnpetrov
 */
public class PostMessage extends VmInstruction {
    private final String message;

    public PostMessage(AstNode loc, String message) {
        super(loc);
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
        return "postMessage \"" + message + "\"" + " @" + getDebugInfo();
    }
}
