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
  public void startClass(String name) {
    try {
      out.write("<h2>" + name + "</h2>\n");
      out.write("<ul>\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** {@inheritDoc} */
  public void endClass(String name) {
    try {
      out.write("</ul>\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** {@inheritDoc} */
  public void onTest(String name) {
    try {
      out.write("<li>" + name + "</li>\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
