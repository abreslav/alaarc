package net.alaarc.ast.nodes;

import java.io.PrintWriter;

/**
 * @author dnpetrov
 */
public class AstAssignStmt extends AstStmt {
    @Override
    public void dump(PrintWriter out, int indent) {
        indentLocation(out, indent).println("assign _ " + operator + " _");
        lhs.dump(out, indent+2);
        rhs.dump(out, indent+2);
    }

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

}
