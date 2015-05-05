package net.alaarc.interpreter;

/**
 * @author dnpetrov
 */
public class RunResult {
    private final int index;
    private final int assertionsPassed;
    private final int assertionsFailed;
    private final int liveObjects;
    private final int alaarcExceptions;

    public RunResult(int index, int assertionsPassed, int assertionsFailed, int liveObjects, int alaarcExceptions) {
        this.index = index;
        this.assertionsPassed = assertionsPassed;
        this.assertionsFailed = assertionsFailed;
        this.liveObjects = liveObjects;
        this.alaarcExceptions = alaarcExceptions;
    }

    public int getIndex() {
        return index;
    }

    public int getAssertionsPassed() {
        return assertionsPassed;
    }

    public int getAssertionsFailed() {
        return assertionsFailed;
    }

    public int getLiveObjects() {
        return liveObjects;
    }

    public int getAlaarcExceptions() {
        return alaarcExceptions;
    }

    @Override
    public String toString() {
        return "Run " + index + ": " +
                "assertions passed: " + assertionsPassed + "; " +
                "assertions failed: " + assertionsFailed + "; " +
                "live objects: " + liveObjects + "; " +
                "exceptions: " + alaarcExceptions
                ;
    }
}
