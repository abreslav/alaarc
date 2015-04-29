package net.alaarc.vm.builders;

import net.alaarc.vm.ComparisonOperator;
import net.alaarc.vm.VmGlobalVar;
import net.alaarc.vm.VmInstruction;
import net.alaarc.vm.VmThreadDef;
import net.alaarc.vm.instructions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * <ul>
 *     <li>
 *         <b>a = object</b>
 *         <p><code>
 *                  t.newObject();
 *                  t.store("a");
 *         </code></p>
 *     </li>
 *     <li>
 *         <b>a.b = x.y</b>
 *         <p><code>
 *                 t.load("x", "y");
 *                 t.store("a", "b");
 *         </code></p>
 *     </li>
 *     <li>
 *         <b>a ~= b</b>
 *         <p><code>
 *                 t.load("b");
 *                 t.storeWeak("a");
 *         </code></p>
 *     </li>
 *     <li>
 *         <b>dump b</b>
 *         <p><code>
 *                 t.load("b");
 *                 t.dump();
 *         </code></p>
 *     </li>
 *     <li>
 *         <b>thread { ... body ... }</b>
 *         <p><code>
 *             t.runThread(tt -> {
 *                 ... body ...
 *             });
 *         </code></p>
 *     </li>
 *
 * </ul>
 *
 * @author dnpetrov
 */
public class VmThreadBuilder {
    private final VmProgramBuilder programBuilder;
    private final int threadId;
    private final List<VmInstruction> body = new ArrayList<>();

    public VmThreadBuilder(int threadId, VmProgramBuilder programBuilder) {
        this.programBuilder = programBuilder;
        this.threadId = threadId;
    }

    public VmThreadDef buildThreadDef() {
        return new VmThreadDef(threadId, body);
    }

    public void dump() {
        body.add(new Dump());
    }

    public void loadGlobal(String varName) {
        body.add(new LoadGlobal(global(varName)));
    }

    private VmGlobalVar global(String varName) {
        return programBuilder.global(varName);
    }

    public void loadSlot(String slotName) {
        body.add(new LoadSlot(slotName));
    }

    public void newObject() {
        body.add(new CreateObject());
    }

    public void postMessage(String message) {
        body.add(new PostMessage(message));
    }

    public void releaseGlobal(String varName) {
        body.add(new ReleaseGlobal(global(varName)));
    }

    public void retainGlobal(String varName) {
        body.add(new RetainGlobal(global(varName)));
    }

    public void runThread(Consumer<VmThreadBuilder> childThread) {
        int id = programBuilder.nextThreadId();
        VmThreadBuilder childBuilder = new VmThreadBuilder(id, programBuilder);
        childThread.accept(childBuilder);
        VmThreadDef childThreadDef = childBuilder.buildThreadDef();
        body.add(new RunThread(childThreadDef));
        programBuilder.addThreadDef(childThreadDef);
    }

    public void sleep() {
        body.add(new Sleep());
    }

    public void sleepRand() {
        body.add(new SleepRand());
    }

    public void storeGlobal(String varName) {
        body.add(new StoreGlobal(global(varName)));
    }

    public void storeSlot(String slotName) {
        body.add(new StoreSlot(slotName));
    }

    public void storeWeakGlobal(String varName) {
        body.add(new StoreWeakGlobal(global(varName)));
    }

    public void storeWeakSlot(String slotName) {
        body.add(new StoreWeakSlot(slotName));
    }

    public void load(String name) {
        loadGlobal(name);
    }

    public void load(String name, String... slots) {
        loadGlobal(name);
        for (String slot : slots) {
            loadSlot(slot);
        }
    }

    public void store(String name, String... slots) {
        if (slots == null || slots.length == 0) {
            storeGlobal(name);
        } else {
            loadGlobal(name);
            int lastSlot = slots.length - 1;
            for (int i = 0; i < lastSlot; ++i) {
                loadSlot(slots[i]);
            }
            storeSlot(slots[lastSlot]);
        }
    }

    public void storeWeak(String name, String... slots) {
        if (slots == null || slots.length == 0) {
            storeWeakGlobal(name);
        } else {
            loadGlobal(name);
            int lastSlot = slots.length - 1;
            for (int i = 0; i < lastSlot; ++i) {
                loadSlot(slots[i]);
            }
            storeWeakSlot(slots[lastSlot]);
        }
    }

    public void assertRc(ComparisonOperator op, long num) {
        body.add(new AssertRc(op, num));
    }

    public void assertRcEq(long num) {
        assertRc(ComparisonOperator.EQ, num);
    }

    public void assertRcNeq(long num) {
        assertRc(ComparisonOperator.NEQ, num);
    }

    public void assertRcLe(long num) {
        assertRc(ComparisonOperator.LE, num);
    }

    public void assertRcLt(long num) {
        assertRc(ComparisonOperator.LT, num);
    }

    public void assertRcGe(long num) {
        assertRc(ComparisonOperator.GE, num);
    }

    public void assertRcGt(long num) {
        assertRc(ComparisonOperator.GT, num);
    }

}
