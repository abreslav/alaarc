package net.alaarc.ast.nodes.exprs;

import net.alaarc.ast.IAstNodeVisitor;
import net.alaarc.ast.nodes.AstExpr;

import java.io.PrintWriter;

/**
 * @author dnpetrov
 */
public class AstNewObjectExpr extends AstExpr {
    public AstNewObjectExpr(String sourceFileName, int lineNumber) {
        super(sourceFileName, lineNumber);
    }

    @Override
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitNewObjectExpr(this);
    }

}
