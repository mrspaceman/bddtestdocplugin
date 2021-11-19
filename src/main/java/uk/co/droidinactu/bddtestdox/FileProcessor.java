package uk.co.droidinactu.bddtestdox;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileProcessor {

  public void processTestClassFiles(
      ProjectSettingsState myState, Project currentProject, VirtualFile srcRoot) {
    final boolean isLicensed = CheckLicense.isLicensed();
    final MultiplexingGenerator gen = new MultiplexingGenerator();
    final FileProcessor fp = new FileProcessor();

    String outputPath =
        currentProject.getBasePath()
            + File.separator
            + (myState.prependProjectName ? currentProject.getName() + "_" : "")
            + myState.outputFilename;

    gen.addGenerator(new ConsoleGenerator());
    if (myState.outputToHtml) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + ".html"))) {
        gen.addGenerator(new HtmlDocumentGenerator(writer));
        gen.startProject(currentProject.getName());
        fp.generateFile(isLicensed, gen, srcRoot);
      } catch (IOException e) {
        e.printStackTrace();
      }
      gen.removeGenerator(HtmlDocumentGenerator.class);
    }
    if (myState.outputToMarkdown) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + ".md"))) {
        gen.addGenerator(new MarkdownDocumentGenerator(writer));
        gen.startProject(currentProject.getName());
        fp.generateFile(isLicensed, gen, srcRoot);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void generateFile(boolean isLicensed, DocumentGenerator gen, VirtualFile testSrcDir) {
    /** Use qdoc for junit tests */
    JavaProjectBuilder builder = new JavaProjectBuilder();
    builder.addSourceTree(new File(testSrcDir.getPath()));
    builder.getSources().forEach(src -> processJUnitTestFile(gen, src));

    if (isLicensed) {
      try {
        /** Use ? for gherkin feature files */
        List<Path> files = new ArrayList<>();
        Files.walk(Paths.get(testSrcDir.getPath()))
            .filter(Files::isRegularFile)
            .filter(f -> f.toString().endsWith(".feature"))
            .forEach(f -> files.add(f));

        files.forEach(
            f -> {
              try {
                processGherkinTestFile(gen, f);
              } catch (IOException e) {
                e.printStackTrace();
              }
            });
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void processGherkinTestFile(DocumentGenerator gen, Path f) throws IOException {
    NameFormatter nameFormatter = new NameFormatter();
    String featureName;
    Stream<String> lines = Files.lines(f);
    lines
        .filter(l -> l.trim().toLowerCase().startsWith("feature:"))
        .forEach(l -> gen.startFeature(nameFormatter.prettifyFeatureName(l)));
    lines = Files.lines(f);
    lines
        .filter(l -> l.trim().toLowerCase().startsWith("scenario:"))
        .forEach(l -> gen.onTest(nameFormatter.prettifyScenarioName(l)));
    lines.close();
  }

  private void processJUnitTestFile(DocumentGenerator gen, JavaSource src) {
    src.getClasses().stream()
        .filter(cls -> UnitTestDetector.isTestClass(cls.getName()))
        .forEach(classObj -> processTestClass(classObj, gen));
  }

  private void processTestClass(JavaClass classObj, DocumentGenerator gen) {
    NameFormatter nameFormatter = new NameFormatter();
    String prettyName = nameFormatter.prettifyTestClass(classObj.getName());
    gen.startClass(prettyName);
    classObj.getMethods().stream()
        .filter(mthdName -> UnitTestDetector.isTestMethod(mthdName.getName()))
        .forEach(mthdName -> gen.onTest(nameFormatter.prettifyTestMethod(mthdName.getName())));
    gen.endClass(prettyName);
  }
}
