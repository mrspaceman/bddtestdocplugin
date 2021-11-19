package uk.co.droidinactu.bddtestdox;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnitTestDetectorTest {

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  public void shouldReturnTrueForTestClasses() {
    assertTrue(UnitTestDetector.isTestClass("class UnitTestDetectorTest"));
    assertTrue(UnitTestDetector.isTestClass("class BddTestDoxServiceTest"));
  }

  @Test
  public void shouldReturnFalseForNonTestClasses() {
    assertFalse(UnitTestDetector.isTestMethod("setUp"));
    assertFalse(UnitTestDetector.isTestClass("class DocumentGenerator"));
    assertFalse(UnitTestDetector.isTestClass("class PopupDialogAction"));
  }

  @Test
  public void shouldReturnTrueForTestMethods() {
    assertTrue(UnitTestDetector.isTestMethod("shouldBeATest"));
    assertTrue(UnitTestDetector.isTestMethod("testIfIsATest"));
  }

  @Test
  public void shouldReturnFalseForNonTestMethods() {
    assertFalse(UnitTestDetector.isTestMethod("setUp"));
    assertFalse(UnitTestDetector.isTestMethod("tearDown"));
    assertFalse(UnitTestDetector.isTestMethod("fooIsNotATest"));
  }
}
