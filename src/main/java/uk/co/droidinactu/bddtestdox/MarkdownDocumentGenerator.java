package uk.co.droidinactu.bddtestdox;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Output's the test results to a markdown file.
 *
 * <p><a href="http://agiledox.sourceforge.net/">Inspired by Chris Stevenson's AgileDox project.</a>
 */
public class MarkdownDocumentGenerator implements DocumentGenerator {
  private final BufferedWriter out;

  public MarkdownDocumentGenerator(BufferedWriter out) {
    this.out = out;
  }

  /** {@inheritDoc} */
  @Override
  public void startProject(String name) {
    try {
      out.write("# Project " + name + "\n\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void startClass(String name) {
    try {
      out.write("## " + name + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void startFeature(String name) {
    try {
      out.write("## " + name + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void endClass(String name) {
    try {
      out.write("\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void onTest(String name) {
    try {
      out.write("  * " + name + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
