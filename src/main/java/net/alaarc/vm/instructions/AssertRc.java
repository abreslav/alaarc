package net.alaarc.vm.instructions;

import net.alaarc.vm.IVmInstructionVisitor;
import net.alaarc.vm.VmInstruction;

/**
 * @author dnpetrov
 */
public class AssertRc extends VmInstruction {
    public enum ComparisonOp {
        EQ("==") {
            @Override
            public boolean compare(long x, long y) {
                return x == y;
            }
        },
        NEQ("!=") {
            @Override
            public boolean compare(long x, long y) {
                return x != y;
            }
        },
        LE("<=") {
            @Override
            public boolean compare(long x, long y) {
                return x <= y;
            }
        },
        LT("<") {
            @Override
            public boolean compare(long x, long y) {
                return x < y;
            }
        },
        GE(">=") {
            @Override
            public boolean compare(long x, long y) {
                return x >= y;
            }
        },
        GT(">") {
            @Override
            public boolean compare(long x, long y) {
                return x > y;
            }
        };

        private final String repr;

        ComparisonOp(String repr) {
            this.repr = repr;
        }

        public abstract boolean compare(long x, long y);

        @Override
        public String toString() {
            return repr;
        }
    }

    private final ComparisonOp comparisonOp;
    private final long number;

    public AssertRc(ComparisonOp comparisonOp, long number) {
        this.comparisonOp = comparisonOp;
        this.number = number;
    }

    public ComparisonOp getComparisonOp() {
        return comparisonOp;
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
        return "ASSERTRC " + comparisonOp + " " + number;
    }
}
