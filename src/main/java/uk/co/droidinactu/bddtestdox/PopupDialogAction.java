package uk.co.droidinactu.bddtestdox;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PopupDialogAction extends AnAction {

  @Override
  public void update(AnActionEvent e) {
    // Using the event, evaluate the context, and enable or disable the action.
    // Set the availability based on whether a project is open
    Project project = e.getProject();
    e.getPresentation().setEnabledAndVisible(project != null);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    ProjectSettingsState myState = ProjectSettingsState.getInstance();
    UnitTestDetector unitTestDetector = new UnitTestDetector();
    MultiplexingGenerator gen = new MultiplexingGenerator();
    NameFormatter prettifier = new NameFormatter();

    // Using the event, create and show a dialog
    @Nullable Project currentProject = event.getProject();

    String rootPath = currentProject.getBasePath();
    @Nullable VirtualFile srcRoot = LocalFileSystem.getInstance().findFileByPath(rootPath);

    if (srcRoot == null) {
      return;
    }

    String outputPath =
        rootPath
            + File.separator
            + (myState.prependProjectName ? currentProject.getName() + "_" : "")
            + myState.outputFilename;

    gen.addGenerator(new ConsoleGenerator());
    if (myState.outputToHtml) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + ".html"))) {
        gen.addGenerator(new HtmlDocumentGenerator(writer));
        generateFile(gen, prettifier, srcRoot, unitTestDetector);
      } catch (IOException e) {
        e.printStackTrace();
      }
      gen.removeGenerator(HtmlDocumentGenerator.class);
    }
    if (myState.outputToMarkdown) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + ".md"))) {
        gen.addGenerator(new MarkdownDocumentGenerator(writer));
        generateFile(gen, prettifier, srcRoot, unitTestDetector);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    VirtualFile dir = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(new File(rootPath));
    if (dir != null) {
      dir.refresh(true, false);
    }
  }

  private void generateFile(
      DocumentGenerator gen,
      NameFormatter prettifier,
      VirtualFile testSrcDir,
      UnitTestDetector unitTestDetector) {
    JavaProjectBuilder builder = new JavaProjectBuilder();
    builder.addSourceTree(new File(testSrcDir.getPath()));
    builder.getSources().forEach(src -> processClasses(gen, prettifier, src, unitTestDetector));
  }

  private void processClasses(
      DocumentGenerator gen,
      NameFormatter prettifier,
      JavaSource src,
      UnitTestDetector unitTestDetector) {
    src.getClasses()
        .forEach(
            classObj -> {
              if (unitTestDetector.isTestClass(classObj.getName())) {
                String prettyName = prettifier.prettifyTestClass(classObj.getName());
                gen.startClass(prettyName);
                processMethods(gen, prettifier, classObj.getMethods(), unitTestDetector);
                gen.endClass(prettyName);
              }
            });
  }

  private void processMethods(
      DocumentGenerator gen,
      NameFormatter prettifier,
      List<JavaMethod> methods,
      UnitTestDetector unitTestDetector) {
    for (int k = 0; k < methods.size(); k++) {

      String name = methods.get(k).getName();
      if (unitTestDetector.isTestMethod(name)) {
        gen.onTest(prettifier.prettifyTestMethod(name));
      }
    }
  }
}
