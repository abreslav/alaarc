package net.alaarc.ast.nodes.stmts;

import net.alaarc.ast.nodes.operators.AstComparisonOperator;
import net.alaarc.ast.IAstNodeVisitor;
import net.alaarc.ast.nodes.AstExpr;
import net.alaarc.ast.nodes.AstStmt;

/**
 * @author dnpetrov
 */
public class AstAssertRcStmt extends AstStmt {

    private final AstComparisonOperator operator;
    private final long number;
    private final AstExpr operand;

    public AstAssertRcStmt(String sourceFileName, int lineNumber, AstExpr operand, AstComparisonOperator operator, long number) {
        super(sourceFileName, lineNumber);
        this.operand = operand;
        this.operator = operator;
        this.number = number;
    }

    public AstComparisonOperator getOperator() {
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
