package net.alaarc;

import org.antlr.v4.runtime.ANTLRInputStream;

import java.io.IOException;

/**
 * @author dnpetrov
 */
public class AntlrTestUtils {
    public static ANTLRInputStream getResourceAsAntlrInput(String path) throws IOException {
        return new ANTLRInputStream(AntlrTestUtils.class.getResourceAsStream(path));
    }
}
