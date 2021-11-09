package uk.co.droidinactu.bddtestdox;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

class UnitTestDetectorTest {

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  public void shouldReturnTrueForTestClasses() {
    UnitTestDetector unitTestDetector = new UnitTestDetector();
    assertTrue(unitTestDetector.isTestClass("class UnitTestDetectorTest"));
    assertTrue(unitTestDetector.isTestClass("class BddTestDoxServiceTest"));
  }

  @Test
  public void shouldReturnFalseForNonTestClasses() {
    UnitTestDetector unitTestDetector = new UnitTestDetector();
    assertFalse(unitTestDetector.isTestMethod("setUp"));
    assertFalse(unitTestDetector.isTestClass("class DocumentGenerator"));
    assertFalse(unitTestDetector.isTestClass("class PopupDialogAction"));
  }

  @Test
  public void shouldReturnTrueForTestMethods() {
    UnitTestDetector unitTestDetector = new UnitTestDetector();
    assertTrue(unitTestDetector.isTestMethod("shouldBeATest"));
    assertTrue(unitTestDetector.isTestMethod("testIfIsATest"));
  }

  @Test
  public void shouldReturnFalseForNonTestMethods() {
    UnitTestDetector unitTestDetector = new UnitTestDetector();
    assertFalse(unitTestDetector.isTestMethod("setUp"));
    assertFalse(unitTestDetector.isTestMethod("tearDown"));
    assertFalse(unitTestDetector.isTestMethod("fooIsNotATest"));
  }
}
