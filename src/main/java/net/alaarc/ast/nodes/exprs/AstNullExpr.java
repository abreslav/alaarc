package net.alaarc.ast.nodes.exprs;

import net.alaarc.ast.IAstNodeVisitor;
import net.alaarc.ast.nodes.AstExpr;

/**
 * @author dnpetrov
 */
public class AstNullExpr extends AstExpr {
    public AstNullExpr(String sourceFileName, int lineNumber) {
        super(sourceFileName, lineNumber);
    }

    @Override
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitNullExpr(this);
    }

}
