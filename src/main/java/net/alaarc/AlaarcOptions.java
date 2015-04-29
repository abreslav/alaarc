package net.alaarc;

import java.util.Optional;

/**
 * Quick and dirty command-line options.
 *
 * @author dnpetrov
 */
public class AlaarcOptions {
    private Optional<String> sourceFileName;
    private Optional<String> logFileName;
    private Optional<String> asmFileName;
    private int times = 1;

    public Optional<String> getSourceFileName() {
        return sourceFileName;
    }

    public Optional<String> getLogFileName() {
        return logFileName;
    }

    public Optional<String> getAsmFileName() {
        return asmFileName;
    }

    public int getTimes() {
        return times;
    }

}
