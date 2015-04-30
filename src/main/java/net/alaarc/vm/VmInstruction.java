package net.alaarc.vm;

import net.alaarc.ast.AstNode;

/**
 * Alaarc VM instruction.
 * For the sake of simplicity, it carries back annotation to the source code.
 *
 * @author dnpetrov
 */
public abstract class VmInstruction {
    private final AstNode loc;

    public VmInstruction(AstNode loc) {
        this.loc = loc;
    }

    public String getDebugInfo() {
        return loc != null ? loc.getSourceFileName() + ":" + loc.getLineNumber() : "<???>";
    }

    public abstract void accept(IVmInstructionVisitor v);
}
