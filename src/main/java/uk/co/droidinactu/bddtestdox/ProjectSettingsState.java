package uk.co.droidinactu.bddtestdox;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Supports storing the application settings in a persistent way. The {@link State} and {@link
 * Storage} annotations define the name of the data and the file name where these persistent
 * application settings are stored.
 */
@State(
    name = "uk.co.droidinactu.bddtestdox.ProjectSettingsState",
    storages = @Storage("BddTestDoxSettingsPlugin.xml"))
public class ProjectSettingsState implements PersistentStateComponent<ProjectSettingsState> {

  public String outputFilename = "output_bddtestdox";
  public boolean prependProjectName = true;

  public static ProjectSettingsState getInstance() {
    return ApplicationManager.getApplication().getService(ProjectSettingsState.class);
  }

  @Nullable
  @Override
  public ProjectSettingsState getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull ProjectSettingsState state) {
    XmlSerializerUtil.copyBean(state, this);
  }
}
