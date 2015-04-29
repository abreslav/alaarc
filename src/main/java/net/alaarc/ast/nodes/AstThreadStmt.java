package net.alaarc.ast.nodes;

import net.alaarc.ast.IAstNodeVisitor;

import java.io.PrintWriter;

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
