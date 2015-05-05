package net.alaarc.ast.nodes;

import net.alaarc.ast.AstNode;
import net.alaarc.ast.IAstNodeVisitor;

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
    public void accept(IAstNodeVisitor visitor) {
        visitor.visitProgram(this);
    }


}
