package net.alaarc.vm;

/**
 * @author dnpetrov
 */
public abstract class VmInstruction {
    public abstract void accept(IVmInstructionVisitor v);
}
