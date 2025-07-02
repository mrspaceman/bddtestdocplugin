package uk.co.droidinactu.bddtestdox;

import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaSource;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <a href="http://agiledox.sourceforge.net/">Adapted from Chris Stevenson's AgileDox project.</a>
 */
public class DocumentGeneratorTest {

  private static final String testFilePostfix = "tests.java";
  private static final String testFilePrefix = "junit";

  private final List<String> messages = new ArrayList<>();

  @Test
  public void testStartClassAndEndClassAreCalled() throws IOException {
    Path rootPath = writeTestToFile();
    String outputPath = rootPath.toString() + File.separator + "test-output-file";

    MultiplexingGenerator gen = new MultiplexingGenerator();
    NameFormatter prettifier = new NameFormatter();
    JavaProjectBuilder builder = new JavaProjectBuilder();
    builder.addSourceTree(rootPath.toFile());

    gen.addGenerator(new Foo());

    builder.getSources().forEach(src -> checkClasses(gen, prettifier, src));

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
      JavaSource src) {
    src.getClasses()
        .forEach(
            classObj -> {
              assertEquals("FooTests", classObj.getName());
              String prettyName = prettifier.prettifyTestClass(classObj.getName());
              gen.startClass(prettyName);
              classObj
                  .getMethods()
                  .forEach(method -> gen.onTest(prettifier.prettifyTestMethod(method.getName())));
              gen.endClass(prettyName);
            });
  }

  public Path writeTestToFile() throws IOException {
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
    /**
     * used to output the name of the project into the generated document.
     *
     * @param name the name of the project
     */
    public void startProject(String name) {
      messages.add("startProject(" + name + ")");
    }

    public void startClass(String name) {
      messages.add("startClass(" + name + ")");
    }

    public void endClass(String name) {
      messages.add("endClass(" + name + ")");
    }

    public void startFeature(String name) {
      messages.add("startFeature(" + name + ")");
    }

    public void onTest(String name) {
      messages.add("onTest(" + name + ")");
    }
  }
}
