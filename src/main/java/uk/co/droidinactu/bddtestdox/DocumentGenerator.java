package uk.co.droidinactu.bddtestdox;

/**
 * <a href="http://agiledox.sourceforge.net/">Adapted from Chris Stevenson's AgileDox project.</a>
 */
public interface DocumentGenerator {

  /**
   * used to output the name of the class into the generated document.
   *
   * @param name the name of the class
   */
  void startClass(String name);

  /**
   * used to output the name of the test into the generated document.
   *
   * @param name the name of the class
   */
  void onTest(String name);

  /**
   * used to output the end marker for a class into the generated document.
   *
   * @param name the name of the class
   */
  void endClass(String name);
}
