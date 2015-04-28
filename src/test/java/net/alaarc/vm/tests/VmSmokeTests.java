package net.alaarc.vm.tests;

import org.junit.Test;

/**
 * Just some "smoke" tests, no real checks.
 *
 * @author dnpetrov
 */
public class VmSmokeTests {
    @Test
    public void helloWorld() {
        VmTestUtils.runProgram(t0 -> {
            t0.postMessage("Hello, world");
        });
    }

    @Test
    public void testWithRC0() {
        VmTestUtils.runProgram(t0 -> {
            // a = object
            t0.newObject();
            t0.store("a");

            // a.b = object
            t0.newObject();
            t0.store("a", "b");

            // a ~= a
            t0.load("a");
            t0.storeWeak("a");
        });
    }

    @Test
    public void testRace1() {
        // This can throw "slot is undefined"
        // (if 'a' is disposed in 't2' before 't1' gets a chance to execute).
        VmTestUtils.runProgram(20, t0 -> {
            // a = object
            t0.newObject();
            t0.store("a");

            // a.b = object
            t0.newObject();
            t0.store("a", "b");

            t0.runThread(t1 -> {
                // sleep
                t1.sleep();

                t1.postMessage("t1 wakes up");

                // a.b.c = object
                t1.newObject();
                t1.store("a", "b", "c");
            });

            t0.runThread(t2 -> {
                // sleep
                t2.sleep();

                t2.postMessage("t2 wakes up");

                // a = object
                t2.newObject();
                t2.store("a");
            });
        });
    }

    @Test
    public void testCyclicWeak() {
        VmTestUtils.runProgram(t0 -> {
            t0.postMessage("a = object");
            t0.newObject();
            t0.store("a");

            t0.postMessage("a.cyclic ~= a");
            t0.load("a");
            t0.storeWeak("a", "cyclic");

            t0.load("a");
            t0.dump();

            t0.postMessage("a ~= a");
            t0.load("a");
            t0.storeWeak("a");
        });
    }

}
