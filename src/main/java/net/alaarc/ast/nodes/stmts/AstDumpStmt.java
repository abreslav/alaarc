package net.alaarc.ast.nodes.stmts;

import net.alaarc.ast.IAstNodeVisitor;
import net.alaarc.ast.nodes.AstExpr;
import net.alaarc.ast.nodes.AstStmt;

/**
 * @author dnpetrov
 */
public class AstDumpStmt extends AstStmt {
    private final AstExpr operand;

    public AstDumpStmt(String sourceFileName, int lineNumber, AstExpr operand) {
        super(sourceFileName, lineNumber);
        this.operand = operand;
    }

    public AstExpr getOperand() {
        return operand;
    }

    @Override
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitDumpStmt(this);
    }

}
