package net.alaarc.compiler;

import net.alaarc.ast.nodes.AstStmt;
import net.alaarc.ast.nodes.AstThreadBody;
import net.alaarc.vm.VmGlobalVar;
import net.alaarc.vm.VmGlobalVarDef;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.VmThreadDef;
import net.alaarc.vm.instructions.ReleaseGlobal;
import net.alaarc.vm.instructions.RetainGlobal;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Generates code for the given thread body, which might be the program itself ("main thread") or a child thread.
 *
 * @author dnpetrov
 */
public class ThreadCodeGenerator {
    private final ProgramCodeGenerator programCodeGenerator;

    private AstThreadBody threadBody;
    private VmThreadDef threadDef;
    private Collection<VmGlobalVarDef> threadUsedVars;

    public ThreadCodeGenerator(ProgramCodeGenerator programCodeGenerator) {
        this.programCodeGenerator = programCodeGenerator;
    }

    public void run(AstThreadBody threadBody) {
        this.threadBody = threadBody;
        this.threadUsedVars = new LinkedHashSet<>();

        int newThreadId = programCodeGenerator.getNewThreadId();

        // Code for each statement will eventually look like:
        //  [<retain_vars>]     -- retains vars captured by a new scope (if there was a new scope)
        //  <stmt_code>         -- code for this particular statement
        //  <release_vars>      -- release variables last used in this statement
        //
        // To collect "last uses", we traverse our thread body in reverse order.
        // NB: In Alaarc, we have linear control flow only (so far).

        Set<VmGlobalVarDef> releasedVars = new HashSet<>();

        // Body blocks in reverse order.
        List<List<VmInstruction>> revBodyBlocks = new ArrayList<>();

        List<AstStmt> reversedBody = new ArrayList<>(threadBody.getStatements());
        Collections.reverse(reversedBody);
        for (AstStmt stmt : reversedBody) {
            StmtCodeGenerator stmtCodeGen = new StmtCodeGenerator(programCodeGenerator, this);
            stmtCodeGen.run(stmt);
            
            Collection<VmGlobalVarDef> stmtUsedVars = stmtCodeGen.getUsedVars();
            
            List<VmInstruction> releaseCode = generateReleaseVars(stmtUsedVars, releasedVars);
            revBodyBlocks.add(releaseCode);

            List<VmInstruction> stmtCode = stmtCodeGen.getStmtCode();
            revBodyBlocks.add(stmtCode);

            if (stmtCodeGen.isRetainCodeRequired()) {
                List<VmInstruction> retainCode = generateRetainCode(stmtUsedVars);
                revBodyBlocks.add(retainCode);
            }

            threadUsedVars.addAll(stmtUsedVars);
        }

        // Join generated body blocks (stored in reverse order).
        List<VmInstruction> body = new ArrayList<>();
        Collections.reverse(revBodyBlocks);
        revBodyBlocks.forEach(body::addAll);

        this.threadDef = new VmThreadDef(newThreadId, body);
    }

    /**
     * Generates "retain code".
     *
     * @param varsToRetain
     * @return code required to retain the given variables
     */
    private List<VmInstruction> generateRetainCode(Collection<VmGlobalVarDef> varsToRetain) {
        return varsToRetain.stream()
                .map(RetainGlobal::new)
                .collect(Collectors.toList());
    }

    /**
     * Generates "release code".
     *
     * @param usedVars
     *      Variables used in a given statement (NB: statements are traversed in reverse order).
     * @param releasedVars
     *      Variables released so far. Updated by this call.
     * @return code required to release the non-released variables
     */
    private List<VmInstruction> generateReleaseVars(Collection<VmGlobalVarDef> usedVars, Set<VmGlobalVarDef> releasedVars) {
        List<VmInstruction> releaseCode = new ArrayList<>();
        usedVars.stream()
                .filter(var -> !releasedVars.contains(var))
                .forEach(var -> {
                    releasedVars.add(var);
                    releaseCode.add(new ReleaseGlobal(var));
                });
        return releaseCode;
    }

    /**
     * Provides the thread definition generated by {@link ThreadCodeGenerator#run(AstThreadBody)}.
     *
     * @return generated thread definition
     */
    public VmThreadDef getThreadDef() {
        return threadDef;
    }

    /**
     * Provides variables used by the thread definition generated by {@link ThreadCodeGenerator#run(AstThreadBody)}.
     *
     * @return variables used by a generated thread definition.
     */
    public Collection<VmGlobalVarDef> getUsedVars() {
        return threadUsedVars;
    }
}


