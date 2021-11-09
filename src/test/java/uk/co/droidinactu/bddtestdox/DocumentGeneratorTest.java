package uk.co.droidinactu.bddtestdox;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaSource;
import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="http://agiledox.sourceforge.net/">Adapted from Chris Stevenson's AgileDox project.</a>
 */

/**
 * Created by IntelliJ IDEA. User: skizz Date: May 9, 2003 Time: 3:38:22 PM To change this template
 * use Options | File Templates.
 */
public class DocumentGeneratorTest extends TestCase {

  private static final String testFilePostfix = "tests.java";
  private static final String testFilePrefix = "junit";

  List messages = new ArrayList();

  @Test
  public void testStartClassAndEndClassAreCalled() throws IOException {

    UnitTestDetector unitTestDetector = new UnitTestDetector();
    Path rootPath = writeTestToFile();
    String outputPath = rootPath.toString() + File.separator + "test-output-file";

    MultiplexingGenerator gen = new MultiplexingGenerator();
    NameFormatter prettifier = new NameFormatter();
    JavaProjectBuilder builder = new JavaProjectBuilder();
    builder.addSourceTree(rootPath.toFile());

    gen.addGenerator(new Foo());

    builder.getSources().forEach(src -> checkClasses(gen, prettifier, unitTestDetector, src));

    assertTrue(messages.contains("startClass(FooTests)"));
    assertTrue(messages.contains("startClass(FooTests)"));
    assertTrue(messages.contains("onTest(finds test method names)"));
    assertTrue(messages.contains("onTest(write test)"));
    assertTrue(messages.contains("onTest(allow test without public)"));

    gen.removeGenerator(Foo.class);
  }

  private void checkClasses(
      DocumentGenerator gen,
      NameFormatter prettifier,
      UnitTestDetector unitTestDetector,
      JavaSource src) {
    src.getClasses()
        .forEach(
            classObj -> {
              assertEquals("FooTests", classObj.getName());
              String prettyName = prettifier.prettifyTestClass(classObj.getName());
              gen.startClass(prettyName);
              classObj
                  .getMethods()
                  .forEach(mthd -> gen.onTest(prettifier.prettifyTestMethod(mthd.getName())));
              gen.endClass(prettyName);
            });
  }

  private Path writeTestToFile() throws IOException {
    File tmpFile = createFile();
    BufferedWriter writer = new BufferedWriter(new FileWriter(tmpFile));
    writer.write("public class FooTests {\n");
    writer.write("\t@Test\n");
    writer.write("\tpublic void testFindsTestMethodNames() {}\n");
    writer.write("\n");
    writer.write("\t@Test\n");
    writer.write("\tpublic void shouldWriteTest() {}\n");
    writer.write("\n");
    writer.write("\t@Test\n");
    writer.write("\t void shouldAllowTestWithoutPublic() {}\n");
    writer.write("\n}\n");
    writer.close();
    return tmpFile.toPath();
  }

  private File createFile() throws IOException {
    return File.createTempFile(testFilePrefix, testFilePostfix);
  }

  private class Foo implements DocumentGenerator {
    public void startClass(String name) {
      messages.add("startClass(" + name + ")");
    }

    public void endClass(String name) {
      messages.add("endClass(" + name + ")");
    }

    public void onTest(String name) {
      messages.add("onTest(" + name + ")");
    }
  }
}
