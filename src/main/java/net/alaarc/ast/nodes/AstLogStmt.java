package net.alaarc.ast.nodes;

import net.alaarc.ast.AstNode;

import java.io.PrintWriter;

/**
 * @author dnpetrov
 */
public class AstLogStmt extends AstNode {
    private final String message;

    public AstLogStmt(String sourceFileName, int lineNumber, String message) {
        super(sourceFileName, lineNumber);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void dump(PrintWriter out, int indent) {
        indentLocation(out, indent).println("log \"" + message + "\"");
    }
}
