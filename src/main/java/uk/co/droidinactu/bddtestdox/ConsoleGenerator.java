package uk.co.droidinactu.bddtestdox;

/**
 * Output's the test results to the console.
 *
 * <p><a href="http://agiledox.sourceforge.net/">Adapted from Chris Stevenson's AgileDox
 * project.</a>
 */
public class ConsoleGenerator implements DocumentGenerator {

  /** {@inheritDoc} */
  public void startClass(String name) {
    System.out.println(name);
  }

  /** {@inheritDoc} */
  public void onTest(String name) {
    System.out.println("    - " + name);
  }

  /** {@inheritDoc} */
  public void endClass(String name) {}
}
