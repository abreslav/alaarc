package net.alaarc.ast.nodes;

import java.io.PrintWriter;

/**
 * @author dnpetrov
 */
public class AstFieldExpr extends AstExpr {
    private final String fieldName;
    private final AstExpr operand;

    public AstFieldExpr(String sourceFileName, int lineNumber, AstExpr operand, String fieldName) {
        super(sourceFileName, lineNumber);
        this.operand = operand;
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public AstExpr getOperand() {
        return operand;
    }

    @Override
    public void dump(PrintWriter out, int indent) {
        indentLocation(out, indent).println("field _." + fieldName);
        operand.dump(out, indent+2);
    }
}
