package net.alaarc.ast.nodes;

import net.alaarc.ast.IAstNodeVisitor;

import java.io.PrintWriter;

/**
 * @author dnpetrov
 */
public class AstAssertRcStmt extends AstStmt {
    public enum ComparisonOperator {
        EQ, NEQ, LE, LT, GE, GT;
    }

    private final ComparisonOperator operator;
    private final long number;
    private final AstExpr operand;

    public AstAssertRcStmt(String sourceFileName, int lineNumber, AstExpr operand, ComparisonOperator operator, long number) {
        super(sourceFileName, lineNumber);
        this.operand = operand;
        this.operator = operator;
        this.number = number;
    }

    public ComparisonOperator getOperator() {
        return operator;
    }

    public long getNumber() {
        return number;
    }

    public AstExpr getOperand() {
        return operand;
    }

    @Override
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitAssertRcStmt(this);
    }

}
