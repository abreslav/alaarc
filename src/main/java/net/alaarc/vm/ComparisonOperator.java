package net.alaarc.vm;

/**
 * @author dnpetrov
 */
public enum ComparisonOperator {
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

    ComparisonOperator(String repr) {
        this.repr = repr;
    }

    public abstract boolean compare(long x, long y);

    @Override
    public String toString() {
        return repr;
    }
}
