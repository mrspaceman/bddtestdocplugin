package uk.co.droidinactu.bddtestdox;

/**
 * reformats the name of a test method to be more readable.
 *
 * <p><a href="http://agiledox.sourceforge.net/">Adapted from Chris Stevenson's AgileDox
 * project.</a>
 */
public class NameFormatter {

  /** test method name prefix */
  public static final String TEST_PREFIX = "test";

  /** alternative test method name prefix */
  public static final String TEST_PREFIX_ALTERNATIVE = "should";

  private String suffix = "Test";
  private String prefix = "Test";

  public String prettifyTestClass(String className) {
    String title = className;
    if (suffix != null && title.endsWith(suffix)) {
      title = title.substring(0, title.lastIndexOf(suffix));
    }
    if (prefix != null && title.startsWith(prefix)) {
      title = title.substring(prefix.length());
    }
    return title;
  }

  public void setTestSuffix(String suffix) {
    this.suffix = suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public String prettifyTestMethod(String testName) {
    String tn2 = testName;
    if (testName.startsWith(TEST_PREFIX)) {
      tn2 = testName.trim().substring(TEST_PREFIX.length());
    } else if (testName.startsWith(TEST_PREFIX_ALTERNATIVE)) {
      tn2 = testName.trim().substring(TEST_PREFIX_ALTERNATIVE.length());
    }
    String[] words = tn2.replace("test", "").split("(?<!^)(?=[A-Z])");
    StringBuilder ftn = new StringBuilder();
    for (String wrd : words) {
      ftn.append(wrd.toLowerCase()).append(" ");
    }
    return ftn.toString().trim();
  }
}
