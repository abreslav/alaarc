package net.alaarc.ast.nodes.stmts;

import net.alaarc.ast.IAstNodeVisitor;
import net.alaarc.ast.nodes.AstStmt;
import net.alaarc.ast.nodes.AstThreadBody;

/**
 * @author dnpetrov
 */
public class AstThreadStmt extends AstStmt {
    private final AstThreadBody body;

    public AstThreadStmt(String sourceFileName, int lineNumber, AstThreadBody body) {
        super(sourceFileName, lineNumber);
        this.body = body;
    }

    public AstThreadBody getBody() {
        return body;
    }

    @Override
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitThreadStmt(this);
    }
}
