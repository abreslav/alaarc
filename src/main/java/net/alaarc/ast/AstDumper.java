package net.alaarc.ast;

import net.alaarc.ast.nodes.*;
import net.alaarc.ast.nodes.exprs.AstFieldExpr;
import net.alaarc.ast.nodes.exprs.AstNameExpr;
import net.alaarc.ast.nodes.exprs.AstNewObjectExpr;
import net.alaarc.ast.nodes.exprs.AstNullExpr;
import net.alaarc.ast.nodes.stmts.*;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author dnpetrov
 */
public class AstDumper implements IAstNodeVisitor {
    private final PrintWriter out;
    private int indent;

    public AstDumper(PrintWriter out, int indent) {
        this.out = out;
        this.indent = indent;
    }

    public static void dump(PrintStream ps, AstNode node, int indent) {
        PrintWriter pw = new PrintWriter(ps);
        dump(pw, node, indent);
    }

    public static void dump(PrintStream ps, AstNode node) {
        PrintWriter pw = new PrintWriter(ps);
        dump(pw, node, 2);
    }

    public static void dump(PrintWriter pw, AstNode node, int indent) {
        AstDumper dumper = new AstDumper(pw, indent);
        node.accept(dumper);
        pw.flush();
    }

    public static void dump(StringWriter sw, AstNode node) {
        PrintWriter pw = new PrintWriter(sw, true);
        AstDumper dumper = new AstDumper(pw, 2);
        node.accept(dumper);
    }

    public static String dumpToString(AstNode node) {
        StringWriter sw = new StringWriter();
        dump(sw, node);
        return sw.toString();
    }

    private static PrintWriter indent(PrintWriter out, int k) {
        for (int i = 0; i < k; ++i) {
            out.print(' ');
        }
        return out;
    }

    private PrintWriter node(AstNode node, String text) {
        indent(out, indent).println(node.getSourceFileName() + ":" + node.getLineNumber() + ": " + text);
        return out;
    }

    private void child(AstNode node) {
        indent += 2;
        node.accept(this);
        indent -= 2;
    }

    private void children(Iterable<? extends AstNode> nodes) {
        indent += 2;
        for (AstNode node : nodes) {
            node.accept(this);
        }
        indent -= 2;
    }

    @Override
    public void visitProgram(AstProgram program) {
        node(program, "program");
        child(program.getMainThreadBody());
    }

    @Override
    public void visitThreadBody(AstThreadBody threadBody) {
        node(threadBody, "threadBody");
        children(threadBody.getStatements());
    }

    @Override
    public void visitAssertRcStmt(AstAssertRcStmt stmt) {
        node(stmt, "assertrc _ " + stmt.getOperator() + " " + stmt.getNumber());
        child(stmt.getOperand());
    }

    @Override
    public void visitAssignStmt(AstAssignStmt stmt) {
        node(stmt, "assign _ " + stmt.getOperator() + " _");
        child(stmt.getLhs());
        child(stmt.getRhs());
    }

    @Override
    public void visitDumpStmt(AstDumpStmt stmt) {
        node(stmt, "dump _");
        child(stmt.getOperand());
    }

    @Override
    public void visitLogStmt(AstLogStmt stmt) {
        node(stmt, "log \"" + stmt.getMessage() + "\"");
    }

    @Override
    public void visitSleepStmt(AstSleepStmt stmt) {
        node(stmt, "sleep");
    }

    @Override
    public void visitSleepRandStmt(AstSleepRandStmt stmt) {
        node(stmt, "sleepr");
    }

    @Override
    public void visitThreadStmt(AstThreadStmt stmt) {
        node(stmt, "thread");
        child(stmt.getBody());
    }

    @Override
    public void visitNullExpr(AstNullExpr expr) {
        node(expr, "null");
    }

    @Override
    public void visitNameExpr(AstNameExpr expr) {
        node(expr, "name " + expr.getName());
    }

    @Override
    public void visitNewObjectExpr(AstNewObjectExpr expr) {
        node(expr, "object");
    }

    @Override
    public void visitFieldExpr(AstFieldExpr expr) {
        node(expr, "field _." + expr.getFieldName());
        child(expr.getOperand());
    }
}
