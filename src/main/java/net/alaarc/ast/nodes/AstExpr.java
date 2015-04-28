package net.alaarc.ast.nodes;

import net.alaarc.ast.AstNode;

/**
 * @author dnpetrov
 */
public abstract class AstExpr extends AstNode {
    public AstExpr(String sourceFileName, int lineNumber) {
        super(sourceFileName, lineNumber);
    }
}
