package uk.co.droidinactu.bddtestdox;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BddTestDoxServiceTest {

  private static final String testFilePostfix = "tests.java";
  private static final String testFilePrefix = "junit";

  private BddTestDoxService bddTestDoxService;
  private String tmpdir;

  @BeforeEach
  void setUp() throws IOException {
    bddTestDoxService = new BddTestDoxService();
    tmpdir = System.getProperty("java.io.tmpdir");

    List<Path> files =
        Files.list(Path.of(tmpdir))
            .filter(path -> path.toString().endsWith(testFilePostfix))
            .collect(Collectors.toList());
    for (Path f : files) {
      if (f.toString().endsWith(testFilePostfix)) {
        Files.delete(f);
        // new File(f.toString()).delete();
      }
    }

    files =
        Files.list(Path.of(tmpdir))
            .filter(path -> path.toString().endsWith(testFilePostfix))
            .collect(Collectors.toList());
    assertEquals(0, files.size());
  }

  @AfterEach
  void tearDown() throws IOException {
    List<Path> files =
        Files.list(Path.of(tmpdir))
            .filter(path -> path.toString().endsWith(testFilePostfix))
            .collect(Collectors.toList());
    for (Path f : files) {
      if (f.toString().startsWith(testFilePrefix) && f.toString().endsWith(testFilePostfix)) {
        Files.delete(f);
      }
    }
  }

  @Test
  public void shouldWriteTestsToFile() throws IOException {
    writeTestToFile();
    String outputFilename = "test-file-name.md";
    bddTestDoxService.writeTestsToFile(outputFilename, tmpdir);
    File f = new File(outputFilename);
    assertTrue(f.exists());
    try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
      String currentLine = reader.readLine();
      assertNotNull(currentLine);
      assertEquals("# Tests Found:", currentLine);

      currentLine = reader.readLine();
      assertNotNull(currentLine);
      assertEquals("", currentLine);

      currentLine = reader.readLine();
      assertNotNull(currentLine);
      assertEquals("## FooTests", currentLine);

      currentLine = reader.readLine();
      assertNotNull(currentLine);
      assertEquals("    * finds test method names", currentLine);

      currentLine = reader.readLine();
      assertNotNull(currentLine);
      assertEquals("    * write test", currentLine);
    }
    Files.delete(Path.of(outputFilename));
  }

  private void writeTestToFile() throws IOException {
    File tmpFile = createFile();
    BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFile));
    writer.write("public class FooTests {\n");
    writer.write("\t@Test\n");
    writer.write("\tpublic void testFindsTestMethodNames() {}\n");
    writer.write("\n");
    writer.write("\t@Test\n");
    writer.write("\tpublic void shouldWriteTest() {}\n");
    writer.write("\n}\n");
    writer.close();
  }

  private File createFile() throws IOException {
    return File.createTempFile(testFilePrefix, testFilePostfix);
  }
}
