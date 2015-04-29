package net.alaarc.ast.nodes;

import net.alaarc.ast.AstNode;
import net.alaarc.ast.IAstNodeVisitor;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dnpetrov
 */
public class AstThreadBody extends AstNode {
    private final List<AstStmt> statements;

    public AstThreadBody(String sourceFileName, int lineNumber, List<AstStmt> statements) {
        super(sourceFileName, lineNumber);
        this.statements = statements;
    }

    @Override
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitThreadBody(this);
    }

    public List<AstStmt> getStatements() {
        return Collections.unmodifiableList(statements);
    }

}
