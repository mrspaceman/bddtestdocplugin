package uk.co.droidinactu.bddtestdox;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ProjectSettingsConfigurable implements Configurable {

  private ProjectSettingsComponent projectSettingsComponent;

  /**
   * Returns the visible name of the configurable component. Note, that this method must return the
   * display name that is equal to the display name declared in XML to avoid unexpected errors.
   *
   * @return the visible name of the configurable component
   */
  @Override
  public @NlsContexts.ConfigurableName String getDisplayName() {
    return "BDD Test Documentation Generator";
  }

  /**
   * Returns the topic in the help file which is shown when help for the configurable is requested.
   *
   * @return the help topic, or {@code null} if no help is available
   */
  @Override
  public @Nullable @NonNls String getHelpTopic() {
    return Configurable.super.getHelpTopic();
  }

  /**
   * Creates new Swing form that enables user to configure the settings. Usually this method is
   * called on the EDT, so it should not take a long time.
   *
   * <p>Also this place is designed to allocate resources (subscriptions/listeners etc.)
   *
   * @return new Swing form to show, or {@code null} if it cannot be created
   * @see #disposeUIResources
   */
  @Override
  public @Nullable JComponent createComponent() {
    projectSettingsComponent = new ProjectSettingsComponent();
    return projectSettingsComponent.getPanel();
  }

  /**
   * Indicates whether the Swing form was modified or not. This method is called very often, so it
   * should not take a long time.
   *
   * @return {@code true} if the settings were modified, {@code false} otherwise
   */
  @Override
  public boolean isModified() {
    ProjectSettingsState settings = ProjectSettingsState.getInstance();
    boolean modified =
        !projectSettingsComponent.getOutputFilenameText().equals(settings.outputFilename);
    modified |= projectSettingsComponent.getPrependProjectFilename() != settings.prependProjectName;
    modified |= projectSettingsComponent.getOutputToHtml() != settings.outputToHtml;
    modified |= projectSettingsComponent.getOutputToMarkdown() != settings.outputToMarkdown;
    return modified;
  }

  /**
   * Stores the settings from the Swing form to the configurable component. This method is called on
   * EDT upon user's request.
   */
  @Override
  public void apply() {
    ProjectSettingsState settings = ProjectSettingsState.getInstance();
    settings.outputFilename = projectSettingsComponent.getOutputFilenameText();
    settings.prependProjectName = projectSettingsComponent.getPrependProjectFilename();
    settings.outputToHtml = projectSettingsComponent.getOutputToHtml();
    settings.outputToMarkdown = projectSettingsComponent.getOutputToMarkdown();
  }

  @Override
  public void reset() {
    ProjectSettingsState settings = ProjectSettingsState.getInstance();
    projectSettingsComponent.setOutputFilenameText(settings.outputFilename);
    projectSettingsComponent.setPrependProjectFilename(settings.prependProjectName);
    projectSettingsComponent.setOutputToHtml(settings.outputToHtml);
    projectSettingsComponent.setOutputToMarkdown(settings.outputToMarkdown);
  }

  @Override
  public void disposeUIResources() {
    projectSettingsComponent = null;
  }
}
