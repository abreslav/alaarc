package net.alaarc.ast.nodes;

import net.alaarc.ast.AstNode;

/**
 * @author dnpetrov
 */
public abstract class AstStmt extends AstNode {
    public AstStmt(String sourceFileName, int lineNumber) {
        super(sourceFileName, lineNumber);
    }
}
