package uk.co.droidinactu.bddtestdox;

/**
 * <a href="http://agiledox.sourceforge.net/">Adapted from Chris Stevenson's AgileDox project.</a>
 */
public class UnitTestDetector {

  /**
   * test to see if a class is a test class.
   *
   * @param className the name of the class to check
   * @return true is the class name indicates that it is a test class, false otherwise
   */
  public boolean isTestClass(String className) {
    return className.endsWith("Test")
        || className.endsWith("Tests")
        || className.endsWith("TestCase")
        || className.startsWith("Test");
  }

  /**
   * test to see if a method is a test method.
   *
   * @param testMethod the name of the method to check
   * @return true is the method name indicates that it is a test, false otherwise
   */
  public boolean isTestMethod(String testMethod) {
    return testMethod.startsWith(NameFormatter.TEST_PREFIX)
        || testMethod.startsWith(NameFormatter.TEST_PREFIX_ALTERNATIVE);
  }
}
