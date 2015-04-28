package net.alaarc.ast.nodes;

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
    public void dump(PrintWriter out, int indent) {
        indentLocation(out, indent).println("thread");
        body.dump(out, indent+2);
    }
}
