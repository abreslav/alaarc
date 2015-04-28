package net.alaarc.ast.nodes;

import net.alaarc.ast.AstNode;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dnpetrov
 */
public class AstThreadBody extends AstNode {
    private final List<AstStmt> statements = new ArrayList<>();

    public AstThreadBody(String sourceFileName, int lineNumber) {
        super(sourceFileName, lineNumber);
    }

    public void addStatement(AstStmt stmt) {
        statements.add(stmt);
    }

    public List<AstStmt> getStatements() {
        return Collections.unmodifiableList(statements);
    }

    @Override
    public void dump(PrintWriter out, int indent) {
        indentLocation(out, indent).println("threadBody");
        for (AstStmt stmt : statements) {
            stmt.dump(out, indent+2);
        }
    }

}
