package net.alaarc.vm.instructions;

import net.alaarc.ast.AstNode;
import net.alaarc.vm.VmGlobalVar;
import net.alaarc.vm.VmGlobalVarDef;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

import java.util.Objects;

/**
 * <code>( --> | 'var' is released, this may cause object release )</code>
 *
 * @author dnpetrov
 */
public class ReleaseGlobal extends VmInstruction {
    private final VmGlobalVarDef globalVar;

    public ReleaseGlobal(AstNode loc, VmGlobalVarDef globalVar) {
        super(loc);
        this.globalVar = Objects.requireNonNull(globalVar);
    }

    public VmGlobalVarDef getGlobalVar() {
        return globalVar;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitReleaseGlobal(this);
    }

    @Override
    public String toString() {
        return "releaseGlobal " + globalVar + " @" + getDebugInfo();
    }
}
