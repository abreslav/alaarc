package net.alaarc.ast;

import net.alaarc.ast.nodes.*;

/**
 * Double-dispatch interface for AST nodes.
 *
 * @author dnpetrov
 */
public interface IAstNodeVisitor {
    void visitNode(AstNode node);

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
