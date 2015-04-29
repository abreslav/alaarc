package net.alaarc.ast.nodes;

import net.alaarc.ast.IAstNodeVisitor;

import java.io.PrintWriter;

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
