package net.alaarc;

import org.antlr.v4.runtime.ANTLRInputStream;

import java.io.IOException;

/**
 * @author dnpetrov
 */
public class TestUtils {
    public static ANTLRInputStream getResourceAsAntlrInput(String path) throws IOException {
        return new ANTLRInputStream(TestUtils.class.getResourceAsStream(path));
    }
}
