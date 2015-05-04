package net.alaarc.compiler;

import net.alaarc.ast.AstNode;
import net.alaarc.ast.nodes.exprs.AstNewObjectExpr;
import net.alaarc.ast.nodes.exprs.AstNullExpr;
import net.alaarc.ast.nodes.operators.AstAssignmentOperator;
import net.alaarc.ast.nodes.operators.AstComparisonOperator;
import net.alaarc.ast.IAstNodeVisitor;
import net.alaarc.ast.nodes.*;
import net.alaarc.ast.nodes.exprs.AstFieldExpr;
import net.alaarc.ast.nodes.exprs.AstNameExpr;
import net.alaarc.ast.nodes.stmts.*;
import net.alaarc.vm.*;
import net.alaarc.vm.instructions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author dnpetrov
 */
public class StmtCodeGenerator {
    private final ProgramCodeGenerator programCodeGenerator;
    private final ThreadCodeGenerator threadCodeGenerator; // unused

    private List<VmInstruction> stmtCode;
    private Collection<VmGlobalVarDef> usedVars;
    private boolean retainCodeRequired;

    private final IAstNodeVisitor stmtVisitor = new StmtCodeGenVisitor();

    public StmtCodeGenerator(ProgramCodeGenerator programCodeGenerator, ThreadCodeGenerator threadCodeGenerator) {
        this.programCodeGenerator = programCodeGenerator;
        this.threadCodeGenerator = threadCodeGenerator;
    }

    public void run(AstStmt stmt) {
        this.stmtCode = new ArrayList<>();
        this.usedVars = new LinkedHashSet<>(2);
        this.retainCodeRequired = isThreadScopeStmt(stmt);

        stmt.accept(stmtVisitor);
    }

    private boolean isThreadScopeStmt(AstStmt stmt) {
        return stmt instanceof AstThreadStmt;
    }

    public List<VmInstruction> getStmtCode() {
        return stmtCode;
    }

    public Collection<VmGlobalVarDef> getUsedVars() {
        return usedVars;
    }

    public boolean isRetainCodeRequired() {
        return retainCodeRequired;
    }

    private void emit(VmInstruction instruction) {
        stmtCode.add(instruction);
    }

    /**
     * Statement code generator.
     * <ul>
     *     <li>for statements, emits corresponding execution code</li>
     *     <li>for expressions, emits evaluation code</li>
     * </ul>
     */
    private class StmtCodeGenVisitor implements IAstNodeVisitor {

        @Override
        public void visitAssertRcStmt(AstAssertRcStmt stmt) {
            stmt.getOperand().accept(this);
            emit(new AssertRc(stmt, getComparisonOperator(stmt.getOperator()), stmt.getNumber()));
        }

        @Override
        public void visitAssignStmt(AstAssignStmt stmt) {
            // Generate code for RHS evaluation
            stmt.getRhs().accept(this);

            // Generate code for LHS. This requires special handling:
            // - if it is a variable, then emit StoreGlobal;
            // - if it is a field, then emit parent evaluation and StoreSlot.
            AstExpr lhs = stmt.getLhs();
            AstAssignmentOperator assignmentOperator = stmt.getOperator();
            emitStoreCode(lhs, assignmentOperator);
        }

        @Override
        public void visitDumpStmt(AstDumpStmt stmt) {
            stmt.getOperand().accept(this);
            emit(new Dump(stmt));
        }

        @Override
        public void visitLogStmt(AstLogStmt stmt) {
            emit(new PostMessage(stmt, stmt.getMessage()));
        }

        @Override
        public void visitSleepStmt(AstSleepStmt stmt) {
            emit(new Sleep(stmt));
        }

        @Override
        public void visitSleepRandStmt(AstSleepRandStmt stmt) {
            emit(new SleepRand(stmt));
        }

        @Override
        public void visitThreadStmt(AstThreadStmt stmt) {
            ThreadCodeGenerator threadCodeGen = new ThreadCodeGenerator(programCodeGenerator);
            threadCodeGen.run(stmt.getBody());
            VmThreadDef threadDef = threadCodeGen.getThreadDef();
            emit(new RunThread(stmt, threadDef.getThreadId()));
            usedVars.addAll(threadCodeGen.getUsedVars());
            programCodeGenerator.addChildThreadDef(threadDef);
        }

        @Override
        public void visitFieldExpr(AstFieldExpr expr) {
            expr.getOperand().accept(this);
            emit(new LoadSlot(expr, expr.getFieldName()));
        }

        @Override
        public void visitNameExpr(AstNameExpr expr) {
            VmGlobalVarDef var = programCodeGenerator.getOrCreateVar(expr.getName());
            usedVars.add(var);
            emit(new LoadGlobal(expr, var));
        }

        @Override
        public void visitNewObjectExpr(AstNewObjectExpr expr) {
            emit(new CreateObject(expr));
        }

        @Override
        public void visitNullExpr(AstNullExpr expr) {
            emit(new PushNull(expr));
        }

        private void emitStoreCode(AstExpr lhs, AstAssignmentOperator assignmentOperator) {
            if (lhs instanceof AstNameExpr) {
                AstNameExpr nameExpr = (AstNameExpr) lhs;
                VmGlobalVarDef var = programCodeGenerator.getOrCreateVar(nameExpr.getName());
                emitStoreGlobal(lhs, var, assignmentOperator);
            } else if (lhs instanceof AstFieldExpr) {
                AstFieldExpr fieldExpr = (AstFieldExpr) lhs;
                // emit parent object evaluation
                fieldExpr.getOperand().accept(this);
                String slotName = fieldExpr.getFieldName();
                emitStoreSlot(lhs, slotName, assignmentOperator);
            } else {
                throw new IllegalArgumentException("Unexpected LHS expression: " + lhs);
            }
        }

        private void emitStoreSlot(AstNode loc, String slotName, AstAssignmentOperator assignmentOperator) {
            // Minor. No switch
            if (assignmentOperator == AstAssignmentOperator.ASSIGN) {
                emit(new StoreSlot(loc, slotName));
            } else if (assignmentOperator == AstAssignmentOperator.ASSIGN_WEAK) {
                emit(new StoreWeakSlot(loc, slotName));
            } else {
                throw new IllegalArgumentException("Unexpected assignment operator: " + assignmentOperator);
            }
        }

        private void emitStoreGlobal(AstNode loc, VmGlobalVarDef var, AstAssignmentOperator assignmentOperator) {
            usedVars.add(var);

            // Minor. No switch
            if (assignmentOperator == AstAssignmentOperator.ASSIGN) {
                emit(new StoreGlobal(loc, var));
            } else if (assignmentOperator == AstAssignmentOperator.ASSIGN_WEAK) {
                emit(new StoreWeakGlobal(loc, var));
            } else {
                throw new IllegalArgumentException("Unexpected assignment operator: " + assignmentOperator);
            }
        }

        /**
         * Translates AST-level comparison operator to VM-level comparison operator.
         *
         * @param operator  AST-level comparison operator
         * @return  VM-level comparison operator
         */
        private ComparisonOperator getComparisonOperator(AstComparisonOperator operator) {
            switch (operator) {
                case EQ:    return ComparisonOperator.EQ;
                case NEQ:   return ComparisonOperator.NEQ;
                case LE:    return ComparisonOperator.LE;
                case LT:    return ComparisonOperator.LT;
                case GE:    return ComparisonOperator.GE;
                case GT:    return ComparisonOperator.GT;
                default:    throw new IllegalArgumentException("Unexpected comparison operator: " + operator);
            }
        }
    }


}
