package net.alaarc.ast.nodes.stmts;

import net.alaarc.ast.IAstNodeVisitor;
import net.alaarc.ast.nodes.AstStmt;

/**
 * @author dnpetrov
 */
public class AstSleepStmt extends AstStmt {
    public AstSleepStmt(String sourceFileName, int lineNumber) {
        super(sourceFileName, lineNumber);
    }

    @Override
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitSleepStmt(this);
    }

}
