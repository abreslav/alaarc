package net.alaarc.vm;

import net.alaarc.vm.instructions.*;

/**
 * Double-dispatch interface for VM instructions.
 *
 * @author dnpetrov
 */
public interface IVmInstructionVisitor {
    void visitLoadGlobal(LoadGlobal instr);

    void visitLoadSlot(LoadSlot instr);

    void visitStoreGlobal(StoreGlobal instr);

    void visitStoreSlot(StoreSlot instr);

    void visitStoreWeakGlobal(StoreWeakGlobal instr);

    void visitStoreWeakSlot(StoreWeakSlot instr);

    void visitSleep(Sleep instr);

    void visitSleepRand(SleepRand instr);

    void visitDump(Dump instr);

    void visitRunThread(RunThread instr);

    void visitNewObject(NewObject instr);

    void visitRetainGlobal(RetainGlobal instr);

    void visitReleaseGlobal(ReleaseGlobal instr);

    void visitPostMessage(PostMessage instr);
}
