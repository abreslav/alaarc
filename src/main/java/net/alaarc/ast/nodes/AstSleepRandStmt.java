package net.alaarc.ast.nodes;

import java.io.PrintWriter;

/**
 * @author dnpetrov
 */
public class AstSleepRandStmt extends AstStmt {
    public AstSleepRandStmt(String sourceFileName, int lineNumber) {
        super(sourceFileName, lineNumber);
    }

    @Override
    public void dump(PrintWriter out, int indent) {
        indentLocation(out, indent).println("sleepr");
    }
}
