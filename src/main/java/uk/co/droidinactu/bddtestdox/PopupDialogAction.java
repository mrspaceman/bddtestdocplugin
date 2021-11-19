package uk.co.droidinactu.bddtestdox;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class PopupDialogAction extends AnAction {
  public PopupDialogAction() {
    super();
  }

  @Override
  public void update(AnActionEvent e) {
    // Using the event, evaluate the context, and enable or disable the action.
    // Set the availability based on whether a project is open
    Project project = e.getProject();
    e.getPresentation().setEnabledAndVisible(project != null);
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    // Using the event, create and show a dialog
    @Nullable Project currentProject = event.getProject();

    String rootPath = currentProject.getBasePath();

    VirtualFile srcRoot =
        LocalFileSystem.getInstance().findFileByPath(rootPath == null ? "./" : rootPath);
    if (srcRoot != null) {
      ProjectSettingsState myState = ProjectSettingsState.getInstance();
      final FileProcessor fp = new FileProcessor();
      fp.processTestClassFiles(myState, currentProject, srcRoot);
      VirtualFile dir =
          LocalFileSystem.getInstance()
              .refreshAndFindFileByIoFile(new File(currentProject.getBasePath()));
      if (dir != null) {
        dir.refresh(true, false);
      }
    }
  }
}
