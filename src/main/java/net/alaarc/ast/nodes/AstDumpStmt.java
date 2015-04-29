package net.alaarc.ast.nodes;

import net.alaarc.ast.IAstNodeVisitor;

import java.io.PrintWriter;

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
