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

    public abstract void dump(PrintWriter out, int indent);

    protected static PrintWriter indent(PrintWriter out, int k) {
        for (int i = 0; i < k; ++i) {
            out.print(' ');
        }
        return out;
    }

    protected void printLocation(PrintWriter out) {
        out.print(sourceFileName + ":" + lineNumber + ": ");
    }

    protected PrintWriter indentLocation(PrintWriter out, int k) {
        indent(out, k);
        printLocation(out);
        return out;
    }
}
