package net.alaarc;


import org.junit.Assert;

import java.io.*;

/**
 * @author dnpetrov
 */
public class FileTestUtils {

    public static void assertContentEquals(String pathToGoldenFile, String actual) {
        try (InputStream golden = getResourceAsStream(pathToGoldenFile)) {
            BufferedReader goldenReader = new BufferedReader(new InputStreamReader(golden));
            BufferedReader actualReader = new BufferedReader(new StringReader(actual));

            int lineNo = 1;

            while (true) {
                String goldenLine = goldenReader.readLine();
                String actualLine = actualReader.readLine();

                if (goldenLine == null && actualLine == null) {
                    Assert.assertTrue("Content matched " + pathToGoldenFile, true);
                    return;
                } else if (goldenLine == null) {
                    assertHasNoNonEmptyLines(actualReader);
                    Assert.assertTrue("Content matched " + pathToGoldenFile, true);
                    return;
                } else if (actualLine == null) {
                    assertHasNoNonEmptyLines(goldenReader);
                    Assert.assertTrue("Content matched " + pathToGoldenFile, true);
                    return;
                } else {
                    Assert.assertEquals("Golden file mismatch at line " + lineNo, goldenLine.trim(), actualLine.trim());
                }

                ++lineNo;
            }

        } catch (IOException e) {
            Assert.fail("Failed to compare with a golden file (" + pathToGoldenFile + "): " + e.getMessage());
        }
    }

    private static void assertHasNoNonEmptyLines(BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                return;
            }
            line = line.trim();
            Assert.assertTrue("Unmatched non-empty line: " + line, line.isEmpty());
        }
    }

    public static InputStream getResourceAsStream(String path) throws IOException {
        return FileTestUtils.class.getResourceAsStream(path);
    }
}
