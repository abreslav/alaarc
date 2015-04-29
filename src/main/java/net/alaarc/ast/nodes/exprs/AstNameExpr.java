package net.alaarc.ast.nodes.exprs;

import net.alaarc.ast.IAstNodeVisitor;
import net.alaarc.ast.nodes.AstExpr;

import java.io.PrintWriter;

/**
 * @author dnpetrov
 */
public class AstNameExpr extends AstExpr {
    private final String name;

    public AstNameExpr(String sourceFileName, int lineNumber, String name) {
        super(sourceFileName, lineNumber);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitNameExpr(this);
    }

}
