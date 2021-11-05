package uk.co.droidinactu.bddtestdox;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class PopupDialogAction extends AnAction {

  private final BddTestDoxService bddTestDoxService = new BddTestDoxService();

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
    String outputPath =
        rootPath
            + File.separator
            + (myState.prependProjectName ? currentProject.getName() + "_" : "")
            + myState.outputFilename
            + ".md";

    try {
      bddTestDoxService.writeTestsToFile(outputPath, rootPath);
    } catch (IOException e) {
      e.printStackTrace();
    }
    VirtualFile dir = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(new File(rootPath));
    if (dir != null) {
      dir.refresh(true, false);
    }
  }
}
