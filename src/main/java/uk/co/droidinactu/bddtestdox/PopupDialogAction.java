package uk.co.droidinactu.bddtestdox;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
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

  public static void requestLicense(final String message) {
    // ensure the dialog is appeared from UI thread and in a non-modal context
    ApplicationManager.getApplication()
        .invokeLater(() -> showRegisterDialog(message), ModalityState.NON_MODAL);
  }

  private static void showRegisterDialog(final String message) {
    final com.intellij.openapi.actionSystem.ActionManager actionManager =
        com.intellij.openapi.actionSystem.ActionManager.getInstance();
    // first, assume we are running inside the opensource version
    AnAction registerAction = actionManager.getAction("RegisterPlugins");
    if (registerAction == null) {
      // assume running inside commercial IDE distribution
      registerAction = actionManager.getAction("Register");
    }
    if (registerAction != null) {
      registerAction.actionPerformed(
          AnActionEvent.createFromDataContext("", new Presentation(), asDataContext(message)));
    }
  }

  // This creates a DataContext providing additional information for the license UI
  // The "Register*" actions show the registration dialog and expect to find this additional data in
  // the DataContext passed to the action
  // - productCode: the product corresponding to the passed productCode will be pre-selected in the
  // opened dialog
  // - message: optional message explaining the reason why the dialog has been shown
  @NotNull
  private static DataContext asDataContext(@Nullable String message) {
    return dataId -> {
      switch (dataId) {
          // the same code as registered in plugin.xml, 'product-descriptor' tag
        case "register.product-descriptor.code":
          return "PPAIDPLUGIN";

          // optional message to be shown in the registration dialog that appears
        case "register.message":
          return message;

        default:
          return null;
      }
    };
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent event) {
    // Using the event, create and show a dialog
    @Nullable Project currentProject = event.getProject();

    String rootPath = currentProject.getBasePath();

    VirtualFile srcRoot = LocalFileSystem.getInstance().findFileByPath(rootPath);
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
