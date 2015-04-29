package net.alaarc.ast.nodes.stmts;

import net.alaarc.ast.nodes.operators.AstAssignmentOperator;
import net.alaarc.ast.IAstNodeVisitor;
import net.alaarc.ast.nodes.AstExpr;
import net.alaarc.ast.nodes.AstStmt;

/**
 * @author dnpetrov
 */
public class AstAssignStmt extends AstStmt {

    private final AstExpr lhs;
    private final AstExpr rhs;
    private final AstAssignmentOperator operator;

    public AstAssignStmt(String sourceFileName, int lineNumber, AstExpr lhs, AstAssignmentOperator operator, AstExpr rhs) {
        super(sourceFileName, lineNumber);
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public AstAssignmentOperator getOperator() {
        return operator;
    }

    public AstExpr getLhs() {
        return lhs;
    }

    public AstExpr getRhs() {
        return rhs;
    }

    @Override
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitAssignStmt(this);
    }

}
