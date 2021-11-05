package uk.co.droidinactu.bddtestdox;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class PopupDialogAction extends AnAction {

  BddTestDoxService bddTestDoxService = new BddTestDoxService();

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
    StringBuilder dlgMsg = new StringBuilder();
    String dlgTitle = event.getPresentation().getDescription();
    // If an element is selected in the editor, add info about it.
    Navigatable nav = event.getData(CommonDataKeys.NAVIGATABLE);
    if (nav != null) {
      dlgMsg.append(String.format("\nClass: %s", nav.toString()));
    }

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
    dlgMsg.append(
        String.format(
            "Test documentation written to file : %s",
            (myState.prependProjectName ? currentProject.getName() + "_" : "")
                + myState.outputFilename
                + ".md"));

    Messages.showMessageDialog(
        currentProject, dlgMsg.toString(), dlgTitle, Messages.getInformationIcon());
  }
}
