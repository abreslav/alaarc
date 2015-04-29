package net.alaarc.ast;

import net.alaarc.ast.nodes.*;
import net.alaarc.ast.nodes.exprs.AstFieldExpr;
import net.alaarc.ast.nodes.exprs.AstNameExpr;
import net.alaarc.ast.nodes.exprs.AstNewObjectExpr;
import net.alaarc.ast.nodes.exprs.AstNullExpr;
import net.alaarc.ast.nodes.stmts.*;

/**
 * Double-dispatch interface for AST nodes.
 *
 * @author dnpetrov
 */
public interface IAstNodeVisitor {
    default void visitNode(AstNode node) {
        throw new IllegalArgumentException("Unexpected node: " + node.toString());
    }

    default void visitProgram(AstProgram program) {
        visitNode(program);
    }

    default void visitThreadBody(AstThreadBody astThreadBody) {
        visitNode(astThreadBody);
    }

    default void visitStmt(AstStmt stmt) {
        visitNode(stmt);
    }

    default void visitAssertRcStmt(AstAssertRcStmt stmt) {
        visitStmt(stmt);
    }

    default void visitAssignStmt(AstAssignStmt stmt) {
        visitStmt(stmt);
    }

    default void visitDumpStmt(AstDumpStmt stmt) {
        visitStmt(stmt);
    }

    default void visitLogStmt(AstLogStmt stmt) {
        visitStmt(stmt);
    }

    default void visitSleepStmt(AstSleepStmt stmt) {
        visitStmt(stmt);
    }

    default void visitSleepRandStmt(AstSleepRandStmt stmt) {
        visitStmt(stmt);
    }

    default void visitThreadStmt(AstThreadStmt stmt) {
        visitStmt(stmt);
    }

    default void visitExpr(AstExpr expr) {
        visitNode(expr);
    }

    default void visitNullExpr(AstNullExpr expr) {
        visitExpr(expr);
    }

    default void visitNameExpr(AstNameExpr expr) {
        visitExpr(expr);
    }

    default void visitNewObjectExpr(AstNewObjectExpr expr) {
        visitExpr(expr);
    }

    default void visitFieldExpr(AstFieldExpr expr) {
        visitExpr(expr);
    }



}
