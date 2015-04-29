package net.alaarc.vm.instructions;

import net.alaarc.vm.ComparisonOperator;
import net.alaarc.vm.IVmInstructionVisitor;
import net.alaarc.vm.VmInstruction;

/**
 * @author dnpetrov
 */
public class AssertRc extends VmInstruction {

    private final ComparisonOperator comparisonOperator;
    private final long number;

    public AssertRc(ComparisonOperator comparisonOperator, long number) {
        this.comparisonOperator = comparisonOperator;
        this.number = number;
    }

    public ComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    public long getNumber() {
        return number;
    }

    @Override
    public void accept(IVmInstructionVisitor v) {
        v.visitAssertRc(this);
    }

    @Override
    public String toString() {
        return "ASSERTRC " + comparisonOperator + " " + number;
    }
}
