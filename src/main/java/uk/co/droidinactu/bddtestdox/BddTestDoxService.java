package uk.co.droidinactu.bddtestdox;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Gets the names of the unit tests for each test file in the given directory and it's
 * subdirectories.
 */
@Slf4j
public class BddTestDoxService {

  /** junit test method annotation (@Test) */
  public static final String JUNIT_TEST_ANNOTATION = "@Test";

  /** test method name prefix */
  public static final String TEST_PREFIX = "test";

  /** alternative test method name prefix */
  public static final String TEST_PREFIX_ALTERNATIVE = "should";

  /** test methods should return void */
  public static final String TEST_FUNCTION_PREFIX_VOID = "void";

  /** test method may be marked as public */
  public static final String TEST_FUNCTION_PREFIX_PUBLIC = "public";

  /**
   * Finds unit test java class files.
   *
   * @param rootPath the root directory to look in (including subdirectories)
   * @return a list of java source file names that end with "test" or "tests"
   * @throws IOException if there is an error reading any of the unt test class files
   */
  public Set<Path> findFiles(String rootPath) throws IOException {
    log.trace("findFiles(\"{}\")", rootPath);
    Set<Path> fileList = new HashSet<>();
    Files.walkFileTree(
        Paths.get(rootPath),
        new SimpleFileVisitor<>() {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            if (!Files.isDirectory(file)
                && (file.toString().toLowerCase().endsWith("tests.java")
                    || file.toString().toLowerCase().endsWith("test.java"))) {
              fileList.add(file);
            }
            return FileVisitResult.CONTINUE;
          }
        });
    return fileList;
  }

  /**
   * takes a list of method names and reformats them from CamelCase to be a list of words with
   * spaces.
   *
   * @param testNames the list of method names to format
   * @return the list of formatted names
   */
  public List<String> formatTestNames(List<String> testNames) {
    log.trace("formatTestNames({})", testNames.stream().collect(Collectors.joining(",")));
    return testNames.parallelStream()
        .map(this::formatTestName)
        .sorted()
        .collect(Collectors.toList());
  }

  /**
   * removes the test name prefix ("test" or "should") and removes everything after the end of the
   * function name ("()" on)
   *
   * @param testName the full test function name
   * @return the formatted test name
   */
  public String formatTestName(String testName) {
    log.trace("formatTestName(\"{}\")", testName);
    String tn2 = testName;
    if (testName.startsWith(TEST_PREFIX)) {
      tn2 = testName.trim().substring(TEST_PREFIX.length());
    } else if (testName.startsWith(TEST_PREFIX_ALTERNATIVE)) {
      tn2 = testName.trim().substring(TEST_PREFIX_ALTERNATIVE.length());
    }
    String[] words = tn2.replace("test", "").split("(?<!^)(?=[A-Z])");
    StringBuilder ftn = new StringBuilder();
    for (String wrd : words) {
      ftn.append(wrd.toLowerCase()).append(" ");
    }
    return ftn.toString().trim();
  }

  /**
   * reads the function method names from inside a java test file.
   *
   * @param fileName the file in which to look for test methods
   * @return a list of test method names
   * @throws IOException if there is an error reading any of the unt test class files
   */
  public List<String> readTestNames(Path fileName) throws IOException {
    log.trace("readTestNames(\"{}\")", fileName.toString());
    ArrayList<String> testNames = new ArrayList<>(1);

    try (BufferedReader reader = new BufferedReader(new FileReader(fileName.toFile()))) {
      String prevLine = "";
      String currentLine;

      while ((currentLine = reader.readLine()) != null) {
        currentLine = currentLine.trim();
        if (prevLine.equals(JUNIT_TEST_ANNOTATION)) {
          log.trace("Found test annotation [\n\t{}\n\t{}\n]", prevLine, currentLine);

          if (currentLine.startsWith(TEST_FUNCTION_PREFIX_VOID)
              || currentLine.startsWith(TEST_FUNCTION_PREFIX_PUBLIC)) {
            log.trace("Found test method [{}]", currentLine);
            currentLine =
                currentLine
                    .substring(
                        currentLine.indexOf(TEST_FUNCTION_PREFIX_VOID)
                            + TEST_FUNCTION_PREFIX_VOID.length(),
                        currentLine.indexOf("()"))
                    .trim();
          }
          if (currentLine.startsWith(TEST_PREFIX)
              || currentLine.startsWith(TEST_PREFIX_ALTERNATIVE)) {
            testNames.add(currentLine);
          }
        }
        prevLine = currentLine;
      }
    }
    return testNames;
  }

  /**
   * gets a map of &lt;classname&gt; --> &lt;list of test names&gt; for all files in the given
   * directory and it's subdirectories.
   *
   * @param testRootDir the root directory to look in (including subdirectories)
   * @return a map of classname to list of test names
   * @throws IOException if there is an error reading any of the * unt test class files
   */
  public Map<String, List<String>> getTestNames(String testRootDir) throws IOException {
    Map<String, List<String>> classTests = new HashMap<>();
    findFiles(testRootDir)
        .forEach(
            testFile -> {
              try {
                classTests.put(
                    FilenameUtils.removeExtension(testFile.toFile().getName()),
                    readTestNames(testFile));
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
    return classTests;
  }

  /**
   * gets the test names and writes them to a file with the given name in Markdown format.
   *
   * @param outputFilename the name of the file to write the tests to
   * @param testRootDir the root directory to look in (including subdirectories)
   * @throws IOException if the file cannot be written (or if there is an error reading any of the
   *     unt test class files)
   */
  public void writeTestsToFile(String outputFilename, String testRootDir) throws IOException {
    log.trace("writeTestsToFile(\"{}\", \"{}\")", outputFilename, testRootDir);
    Map<String, List<String>> classTests = getTestNames(testRootDir);
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename))) {
      writer.write("# Tests Found:");
      writer.write("\n\n");
      classTests
          .keySet()
          .forEach(
              className -> {
                try {
                  writer.write("## " + className);
                  writer.write("\n");
                  writeTestNamesToFile(classTests.get(className), writer);
                  writer.write("");
                  writer.write("\n");
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });
    }
  }

  private void writeTestNamesToFile(List<String> testNames, BufferedWriter writer) {
    formatTestNames(testNames)
        .forEach(
            tn -> {
              try {
                writer.write(("    * " + tn));
                writer.write("\n");
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
  }
}
