package uk.co.droidinactu.bddtestdox;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Output's the test results to an html file.
 *
 * <p><a href="http://agiledox.sourceforge.net/">Adapted from Chris Stevenson's AgileDox
 * project.</a>
 */
public class HtmlDocumentGenerator implements DocumentGenerator {
  private BufferedWriter out;

  public HtmlDocumentGenerator(BufferedWriter out) {
    this.out = out;
  }

  /** {@inheritDoc} */
  @Override
  public void startProject(String name) {
    try {
      out.write("<h1>Project " + name + "</h1>\n\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void startClass(String name) {
    try {
      out.write("<h2>" + name + "</h2>\n");
      out.write("<ul>\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void startFeature(String name) {
    try {
      out.write("<h2>" + name + "</h2>\n");
      out.write("<ul>\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void endClass(String name) {
    try {
      out.write("</ul>\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** {@inheritDoc} */
  @Override
  public void onTest(String name) {
    try {
      out.write("<li>" + name + "</li>\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
