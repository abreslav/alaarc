package net.alaarc.ast.nodes;

import net.alaarc.ast.AstNode;
import net.alaarc.ast.IAstNodeVisitor;

/**
 * @author dnpetrov
 */
public abstract class AstExpr extends AstNode {
    public AstExpr(String sourceFileName, int lineNumber) {
        super(sourceFileName, lineNumber);
    }

    @Override
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitExpr(this);
    }
}
