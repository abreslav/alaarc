package net.alaarc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Optional;

/**
 * Quick and dirty command-line options.
 *
 * @author dnpetrov
 */
public class AlaarcOptions {
    private Optional<String> sourceFileName = Optional.empty();
    private Optional<String> logFileName = Optional.of("stdout");
    private Optional<String> asmFileName = Optional.of("stdout");
    private int times = 1;

    public Optional<String> getSourceFileName() {
        return sourceFileName;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = Optional.of(sourceFileName);
    }

    public Optional<String> getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = Optional.of(logFileName);
    }

    public Optional<String> getAsmFileName() {
        return asmFileName;
    }

    public void setAsmFileName(String asmFileName) {
        this.asmFileName = Optional.of(asmFileName);
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public static OutputStream resolveOutputStream(String fileName) throws FileNotFoundException {
        if (fileName.equals("stdout")) {
            return System.out;
        } else if (fileName.equals("stderr")) {
            return System.err;
        } else {
            return new FileOutputStream(fileName);
        }
    }

}
