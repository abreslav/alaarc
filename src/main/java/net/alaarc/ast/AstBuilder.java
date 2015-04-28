package net.alaarc.ast;

import net.alaarc.ast.nodes.*;
import net.alaarc.grammar.AlaarcBaseVisitor;
import net.alaarc.grammar.AlaarcParser;
import net.alaarc.vm.instructions.AssertRc;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

/**
 * Builds AST from ANTLR parse tree.
 *
 * @author dnpetrov
 */
public class AstBuilder extends AlaarcBaseVisitor<AstNode> {
    private final String sourceFileName;

    public AstBuilder(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    public AstProgram buildProgram(AlaarcParser.InitContext ctx) {
        return (AstProgram) visitInit(ctx);
    }

    @Override
    public AstNode visitInit(AlaarcParser.InitContext ctx) {
        AstThreadBody body = (AstThreadBody) visitThreadBody(ctx.threadBody());
        return new AstProgram(sourceFileName, getLineNumber(ctx), body);
    }

    @Override
    public AstNode visitThreadBody(AlaarcParser.ThreadBodyContext ctx) {
        AstThreadBody body = new AstThreadBody(sourceFileName, getLineNumber(ctx));
        for (AlaarcParser.StmtContext stmtc : ctx.stmt()) {
            AstStmt stmt = (AstStmt) visitStmt(stmtc);
            body.addStatement(stmt);
        }
        return body;
    }

    @Override
    public AstNode visitStmt(AlaarcParser.StmtContext ctx) {
        if (ctx.assignStmt() != null) {
            return visitAssignStmt(ctx.assignStmt());
        } else if (ctx.threadStmt() != null) {
            return visitThreadStmt(ctx.threadStmt());
        } else if (ctx.sleepStmt() != null) {
            return visitSleepStmt(ctx.sleepStmt());
        } else if (ctx.sleepRandStmt() != null) {
            return visitSleepRandStmt(ctx.sleepRandStmt());
        } else if (ctx.dumpStmt() != null) {
            return visitDumpStmt(ctx.dumpStmt());
        } else if (ctx.logStmt() != null) {
            return visitLogStmt(ctx.logStmt());
        } else if (ctx.assertRcStmt() != null) {
            return visitAssertRcStmt(ctx.assertRcStmt());
        } else {
            throw new RuntimeException("Unexpected statement: " + getDiagnostics(ctx));
        }
    }

    private String getDiagnostics(ParserRuleContext ctx) {
        return sourceFileName + ":" + getLineNumber(ctx) + ": " + ctx.toStringTree();
    }

    @Override
    public AstNode visitAssignStmt(AlaarcParser.AssignStmtContext ctx) {
        AstExpr lhs = (AstExpr) visitLvExpr(ctx.lvExpr());
        AstExpr rhs = (AstExpr) visitRvExpr(ctx.rvExpr());
        AstAssignStmt.AssignmentOperator assignOp = getAssignmentOperator(ctx.assignOp());
        return new AstAssignStmt(sourceFileName, getLineNumber(ctx.assignOp()), lhs, assignOp, rhs);
    }

    @Override
    public AstNode visitThreadStmt(AlaarcParser.ThreadStmtContext ctx) {
        AstThreadBody threadBody = (AstThreadBody) visitThreadBody(ctx.threadBody());
        return new AstThreadStmt(sourceFileName, getLineNumber(ctx), threadBody);
    }

    @Override
    public AstNode visitSleepStmt(AlaarcParser.SleepStmtContext ctx) {
        return new AstSleepStmt(sourceFileName, getLineNumber(ctx));
    }

    @Override
    public AstNode visitSleepRandStmt(AlaarcParser.SleepRandStmtContext ctx) {
        return new AstSleepRandStmt(sourceFileName, getLineNumber(ctx));
    }

    @Override
    public AstNode visitDumpStmt(AlaarcParser.DumpStmtContext ctx) {
        AstExpr expr = (AstExpr) visitLvExpr(ctx.lvExpr());
        return new AstDumpStmt(sourceFileName, getLineNumber(ctx), expr);
    }

    @Override
    public AstNode visitLogStmt(AlaarcParser.LogStmtContext ctx) {
        String str = ctx.STRING().getText();
        str = str.substring(1, str.length() - 1); // drop quotes
        return new AstLogStmt(sourceFileName, getLineNumber(ctx), str);
    }

    @Override
    public AstNode visitAssertRcStmt(AlaarcParser.AssertRcStmtContext ctx) {
        AstExpr expr = (AstExpr) visitLvExpr(ctx.lvExpr());
        AstAssertRcStmt.ComparisonOperator operator = getComparisonOperator(ctx.comparisonOp());
        long number = parseIntegral(ctx.INT());
        return new AstAssertRcStmt(sourceFileName, getLineNumber(ctx), expr, operator, number);
    }

    private AstAssertRcStmt.ComparisonOperator getComparisonOperator(AlaarcParser.ComparisonOpContext ctx) {
        if (ctx.EQ() != null) {
            return AstAssertRcStmt.ComparisonOperator.EQ;
        } else if (ctx.NEQ() != null) {
            return AstAssertRcStmt.ComparisonOperator.NEQ;
        } else if (ctx.LE() != null) {
            return AstAssertRcStmt.ComparisonOperator.LE;
        } else if (ctx.LT() != null) {
            return AstAssertRcStmt.ComparisonOperator.LT;
        } else if (ctx.GE() != null) {
            return AstAssertRcStmt.ComparisonOperator.GE;
        } else if (ctx.GT() != null) {
            return AstAssertRcStmt.ComparisonOperator.GT;
        } else {
            throw new RuntimeException("Unexpected comparison operator: " + getDiagnostics(ctx));
        }
    }

    private long parseIntegral(TerminalNode node) {
        return Long.valueOf(node.getText());
    }

    @Override
    public AstNode visitLvExpr(AlaarcParser.LvExprContext ctx) {
        String varName = ctx.ID().getText();
        AstExpr expr = new AstNameExpr(sourceFileName, getLineNumber(ctx.ID()), varName);
        for (AlaarcParser.FieldDerefContext fieldDeref : ctx.fieldDeref()) {
            String fieldName = fieldDeref.ID().getText();
            expr = new AstFieldExpr(sourceFileName, getLineNumber(fieldDeref.DOT()), expr, fieldName);
        }
        return expr;
    }

    @Override
    public AstNode visitRvExpr(AlaarcParser.RvExprContext ctx) {
        if (ctx.lvExpr() != null) {
            return visitLvExpr(ctx.lvExpr());
        } else if (ctx.newObjectExpr() != null) {
            return visitNewObjectExpr(ctx.newObjectExpr());
        } else if (ctx.nullExpr() != null) {
            return visitNullExpr(ctx.nullExpr());
        } else {
            throw new RuntimeException("Unexpected expression: " + getDiagnostics(ctx));
        }
    }

    @Override
    public AstNode visitNewObjectExpr(AlaarcParser.NewObjectExprContext ctx) {
        return new AstNewObjectExpr(sourceFileName, getLineNumber(ctx));
    }

    @Override
    public AstNode visitNullExpr(AlaarcParser.NullExprContext ctx) {
        return new AstNullExpr(sourceFileName, getLineNumber(ctx));
    }

    private int getLineNumber(TerminalNode terminalNode) {
        return terminalNode.getSymbol().getLine();
    }

    private int getLineNumber(ParserRuleContext ctx) {
        return ctx.getStart().getLine();
    }

    private AstAssignStmt.AssignmentOperator getAssignmentOperator(AlaarcParser.AssignOpContext ctx) {
        if (ctx.ASSIGN() != null) {
            return AstAssignStmt.AssignmentOperator.ASSIGN;
        } else if (ctx.ASSIGN_WEAK() != null) {
            return AstAssignStmt.AssignmentOperator.ASSIGN_WEAK;
        } else {
            throw new RuntimeException("Unexpected assignment operator: " + getDiagnostics(ctx));
        }
    }
}
