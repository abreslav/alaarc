package net.alaarc.ast;

import java.io.PrintWriter;

/**
 * @author dnpetrov
 */
public abstract class AstNode {
    private final String sourceFileName;
    private final int lineNumber;

    public AstNode(String sourceFileName, int lineNumber) {
        this.sourceFileName = sourceFileName;
        this.lineNumber = lineNumber;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public abstract void accept(IAstNodeVisitor visitor);

}
