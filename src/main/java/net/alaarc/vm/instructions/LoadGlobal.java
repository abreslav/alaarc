package net.alaarc.vm.instructions;

import net.alaarc.ast.AstNode;
import net.alaarc.vm.VmGlobalVar;
import net.alaarc.vm.VmGlobalVarDef;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

import java.util.Objects;

/**
 * <code>( --> [var] )</code>
 *
 * @author dnpetrov
 */
public class LoadGlobal extends VmInstruction {
    private final VmGlobalVarDef globalVar;

    public LoadGlobal(AstNode loc, VmGlobalVarDef globalVar) {
        super(loc);
        this.globalVar = Objects.requireNonNull(globalVar);
    }

    public VmGlobalVarDef getGlobalVar() {
        return globalVar;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitLoadGlobal(this);
    }

    @Override
    public String toString() {
        return "loadGlobal " + globalVar.getName() + " @" + getDebugInfo();
    }
}
