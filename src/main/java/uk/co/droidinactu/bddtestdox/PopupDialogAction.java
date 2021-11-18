package uk.co.droidinactu.bddtestdox;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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

    // Using the event, create and show a dialog
    @Nullable Project currentProject = event.getProject();

    String rootPath = currentProject.getBasePath();

    VirtualFile srcRoot = LocalFileSystem.getInstance().findFileByPath(rootPath);
    if (srcRoot != null) {
      processTestClassFiles(myState, currentProject, srcRoot);
      VirtualFile dir =
          LocalFileSystem.getInstance()
              .refreshAndFindFileByIoFile(new File(currentProject.getBasePath()));
      if (dir != null) {
        dir.refresh(true, false);
      }
    }
  }

  public void processTestClassFiles(
      ProjectSettingsState myState, Project currentProject, VirtualFile srcRoot) {
    MultiplexingGenerator gen = new MultiplexingGenerator();
    FileProcessor fp = new FileProcessor();

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
        fp.generateFile(gen, srcRoot);
      } catch (IOException e) {
        e.printStackTrace();
      }
      gen.removeGenerator(HtmlDocumentGenerator.class);
    }
    if (myState.outputToMarkdown) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + ".md"))) {
        gen.addGenerator(new MarkdownDocumentGenerator(writer));
        gen.startProject(currentProject.getName());
        fp.generateFile(gen, srcRoot);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
