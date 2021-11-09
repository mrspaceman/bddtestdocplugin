package uk.co.droidinactu.bddtestdox;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Output's the test results to a markdown file.
 *
 * <p><a href="http://agiledox.sourceforge.net/">Inspired by Chris Stevenson's AgileDox project.</a>
 */
public class MarkdownDocumentGenerator implements DocumentGenerator {
  private BufferedWriter out;

  public MarkdownDocumentGenerator(BufferedWriter out) {
    this.out = out;
  }

  /** {@inheritDoc} */
  public void startClass(String name) {
    try {
      out.write("## " + name + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** {@inheritDoc} */
  public void endClass(String name) {
    try {
      out.write("\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** {@inheritDoc} */
  public void onTest(String name) {
    try {
      out.write("  * " + name + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
