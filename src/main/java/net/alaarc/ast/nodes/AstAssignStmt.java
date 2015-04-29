package net.alaarc.ast.nodes;

import net.alaarc.ast.IAstNodeVisitor;

import java.io.PrintWriter;

/**
 * @author dnpetrov
 */
public class AstAssignStmt extends AstStmt {
    public enum AssignmentOperator {
        ASSIGN,
        ASSIGN_WEAK
    }

    private final AstExpr lhs;
    private final AstExpr rhs;
    private final AssignmentOperator operator;

    public AstAssignStmt(String sourceFileName, int lineNumber, AstExpr lhs, AssignmentOperator operator, AstExpr rhs) {
        super(sourceFileName, lineNumber);
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public AssignmentOperator getOperator() {
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
