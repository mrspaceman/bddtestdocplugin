package uk.co.droidinactu.bddtestdox;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/** Supports creating and managing a {@link JPanel} for the Settings Dialog. */
public class ProjectSettingsComponent {

  private final JPanel myMainPanel;
  private final JBTextField myOutputFilenameText = new JBTextField();
  private final JBCheckBox prependProjectFilename =
      new JBCheckBox("Prepend project name to file? ");
  private final JBCheckBox outputToHtml = new JBCheckBox("Output HTML format? ");
  private final JBCheckBox outputToMarkdown = new JBCheckBox("Output Markdown format? ");

  public ProjectSettingsComponent() {
    myMainPanel =
        FormBuilder.createFormBuilder()
            .addLabeledComponent(
                new JBLabel("Enter output filename: "), myOutputFilenameText, 1, false)
            .addComponent(prependProjectFilename, 1)
            .addComponent(outputToHtml, 1)
            .addComponent(outputToMarkdown, 1)
            .addComponentFillVertically(new JPanel(), 0)
            .getPanel();
  }

  public JPanel getPanel() {
    return myMainPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return myOutputFilenameText;
  }

  @NotNull
  public String getOutputFilenameText() {
    return myOutputFilenameText.getText();
  }

  public void setOutputFilenameText(@NotNull String newText) {
    myOutputFilenameText.setText(newText);
  }

  public boolean getPrependProjectFilename() {
    return prependProjectFilename.isSelected();
  }

  public void setPrependProjectFilename(boolean newStatus) {
    prependProjectFilename.setSelected(newStatus);
  }

  public boolean getOutputToHtml() {
    return outputToHtml.isSelected();
  }

  public void setOutputToHtml(boolean newStatus) {
    outputToHtml.setSelected(newStatus);
  }

  public boolean getOutputToMarkdown() {
    return outputToMarkdown.isSelected();
  }

  public void setOutputToMarkdown(boolean newStatus) {
    outputToMarkdown.setSelected(newStatus);
  }
}
