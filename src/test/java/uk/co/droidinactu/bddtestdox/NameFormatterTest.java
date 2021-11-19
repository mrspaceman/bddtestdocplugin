package uk.co.droidinactu.bddtestdox;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <a href="http://agiledox.sourceforge.net/">Adapted from Chris Stevenson's AgileDox project.</a>
 */
class NameFormatterTest {

  private NameFormatter nameFormatter;

  @BeforeEach
  void setUp() {
    nameFormatter = new NameFormatter();
  }

  @AfterEach
  void tearDown() {}

  @Test
  public void testTitleHasSensibleDefaults() {
    assertEquals("Foo", nameFormatter.prettifyTestClass("FooTest"));
    assertEquals("Foo", nameFormatter.prettifyTestClass("TestFoo"));
    assertEquals("Foo", nameFormatter.prettifyTestClass("TestFooTest"));
  }

  @Test
  public void testCaterForUserDefinedSuffix() {
    nameFormatter.setSuffix("TestCase");
    nameFormatter.setPrefix(null);
    assertEquals("Foo", nameFormatter.prettifyTestClass("FooTestCase"));
    assertEquals("TestFoo", nameFormatter.prettifyTestClass("TestFoo"));
    assertEquals("FooTest", nameFormatter.prettifyTestClass("FooTest"));
  }

  @Test
  public void testCaterForUserDefinedPrefix() {
    nameFormatter.setSuffix(null);
    nameFormatter.setPrefix("XXX");
    assertEquals("Foo", nameFormatter.prettifyTestClass("XXXFoo"));
    assertEquals("TestXXX", nameFormatter.prettifyTestClass("TestXXX"));
    assertEquals("XXX", nameFormatter.prettifyTestClass("XXXXXX"));
  }

  @Test
  public void testTestNameIsConvertedToASentence() {
    assertEquals("this is a test", nameFormatter.prettifyTestMethod("testThisIsATest"));
    assertEquals(
        "database_column_spec is set correctly",
        nameFormatter.prettifyTestMethod("testdatabase_column_specIsSetCorrectly"));
  }
}
