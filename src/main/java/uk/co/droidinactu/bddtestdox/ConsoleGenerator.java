package uk.co.droidinactu.bddtestdox;

/**
 * Output's the test results to the console.
 *
 * <p><a href="http://agiledox.sourceforge.net/">Adapted from Chris Stevenson's AgileDox
 * project.</a>
 */
public class ConsoleGenerator implements DocumentGenerator {

  /** {@inheritDoc} */
  @Override
  public void startProject(String name) {
    System.out.println("Project: " + name);
  }

  /** {@inheritDoc} */
  @Override
  public void startClass(String name) {
    System.out.println(name);
  }

  /** {@inheritDoc} */
  @Override
  public void onTest(String name) {
    System.out.println("    - " + name);
  }

  /** {@inheritDoc} */
  @Override
  public void endClass(String name) {}
}
