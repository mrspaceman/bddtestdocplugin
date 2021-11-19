package uk.co.droidinactu.bddtestdox;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PopupDialogActionTest {

  private final DocumentGeneratorTest documentGeneratorTest = new DocumentGeneratorTest();
  private FileProcessor fileProcessor;
  private ProjectSettingsState myState;

  @Mock private Project currentProject;

  @Mock private VirtualFile srcRoot;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);

    myState = new ProjectSettingsState();
    myState.outputToMarkdown = true;
    myState.outputToHtml = true;
    myState.prependProjectName = true;
    myState.outputFilename = "TestDocCreation";
    PopupDialogAction popupDialogAction = new PopupDialogAction();
    fileProcessor = new FileProcessor();

    Mockito.when(currentProject.getName()).thenReturn("TestApp");
  }

  @AfterEach
  void tearDown() {}

  @Test
  void processTestClassFiles() throws IOException {
    Path rootPath = documentGeneratorTest.writeTestToFile();

    Mockito.when(currentProject.getBasePath()).thenReturn("./");
    Mockito.when(srcRoot.getPath()).thenReturn(rootPath.getParent().toString());
    fileProcessor.processTestClassFiles(myState, currentProject, srcRoot);

    // open file
    String outputFilename = currentProject.getName() + "_" + myState.outputFilename + ".md";
    File f = new File(outputFilename);
    assertTrue(f.exists());
    try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
      String currentLine = reader.readLine();
      assertNotNull(currentLine);
      assertEquals("# Project TestApp", currentLine);

      currentLine = reader.readLine();
      assertNotNull(currentLine);
      assertEquals("", currentLine);

      currentLine = reader.readLine();
      assertNotNull(currentLine);
      assertEquals("## FooTests", currentLine);

      currentLine = reader.readLine();
      assertNotNull(currentLine);
      assertEquals("  * finds test method names", currentLine);

      currentLine = reader.readLine();
      assertNotNull(currentLine);
      assertEquals("  * write test", currentLine);
    }
    Files.delete(rootPath);
    Files.delete(Path.of(outputFilename));
  }
}
