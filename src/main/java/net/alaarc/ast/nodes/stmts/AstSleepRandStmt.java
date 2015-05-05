package net.alaarc.ast.nodes.stmts;

import net.alaarc.ast.IAstNodeVisitor;
import net.alaarc.ast.nodes.AstStmt;

/**
 * @author dnpetrov
 */
public class AstSleepRandStmt extends AstStmt {
    public AstSleepRandStmt(String sourceFileName, int lineNumber) {
        super(sourceFileName, lineNumber);
    }

    @Override
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitSleepRandStmt(this);
    }

}
