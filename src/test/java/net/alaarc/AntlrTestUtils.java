package net.alaarc;

import org.antlr.v4.runtime.ANTLRInputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author dnpetrov
 */
public class AntlrTestUtils {
    public static ANTLRInputStream getResourceAsAntlrInput(String path) throws IOException {
        return new ANTLRInputStream(FileTestUtils.getResourceAsStream(path));
    }

}
