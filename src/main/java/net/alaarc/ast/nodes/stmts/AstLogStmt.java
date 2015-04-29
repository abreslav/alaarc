package net.alaarc.ast.nodes.stmts;

import net.alaarc.ast.AstNode;
import net.alaarc.ast.IAstNodeVisitor;
import net.alaarc.ast.nodes.AstStmt;

import java.io.PrintWriter;

/**
 * @author dnpetrov
 */
public class AstLogStmt extends AstStmt {
    private final String message;

    public AstLogStmt(String sourceFileName, int lineNumber, String message) {
        super(sourceFileName, lineNumber);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitLogStmt(this);
    }

}
