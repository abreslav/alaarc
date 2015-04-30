package net.alaarc.vm.instructions;

import net.alaarc.ast.AstNode;
import net.alaarc.vm.VmGlobalVar;
import net.alaarc.vm.VmGlobalVarDef;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.IVmInstructionVisitor;

import java.util.Objects;

/**
 * <code>( a --> | [var ~= a] )</code>
 *
 * @author dnpetrov
 */
public class StoreWeakGlobal extends VmInstruction {
    private final VmGlobalVarDef globalVar;

    public StoreWeakGlobal(AstNode loc, VmGlobalVarDef globalVar) {
        super(loc);
        this.globalVar = Objects.requireNonNull(globalVar);
    }

    public VmGlobalVarDef getGlobalVar() {
        return globalVar;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitStoreWeakGlobal(this);
    }

    @Override
    public String toString() {
        return "storeWeakGlobal " + globalVar.getName() + " @" + getDebugInfo();
    }
}
