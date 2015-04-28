package net.alaarc.ast.nodes;

import net.alaarc.ast.AstNode;

import java.io.PrintWriter;

/**
 * @author dnpetrov
 */
public class AstProgram extends AstNode {
    private final AstThreadBody mainThreadBody;

    public AstProgram(String sourceFileName, int lineNumber, AstThreadBody mainThreadBody) {
        super(sourceFileName, lineNumber);
        this.mainThreadBody = mainThreadBody;
    }

    public AstThreadBody getMainThreadBody() {
        return mainThreadBody;
    }

    @Override
    public void dump(PrintWriter out, int indent) {
        indentLocation(out, indent).println("program");
        mainThreadBody.dump(out, indent+2);
    }
}
