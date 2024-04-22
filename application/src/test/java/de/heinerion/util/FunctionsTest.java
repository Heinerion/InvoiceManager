package de.heinerion.util;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class FunctionsTest {

  @Test
  void doNothing() {
    // Nothing done - nothing to test, but also no exceptions
    Functions.doNothing("a", "b");
    Functions.doNothing(null, 42);
    Functions.doNothing("a", 42);
    Functions.doNothing(17D, null);
  }

  @Test
  void alwaysTrue() {
    assertTrue(Functions.alwaysTrue(true));
    assertTrue(Functions.alwaysTrue(false));
    assertTrue(Functions.alwaysTrue("test"));
    assertTrue(Functions.alwaysTrue(null));
  }

  @Test
  void alwaysFalse() {
    assertFalse(Functions.alwaysFalse(true));
    assertFalse(Functions.alwaysFalse(false));
    assertFalse(Functions.alwaysFalse("test"));
    assertFalse(Functions.alwaysFalse(null));
  }

  @Test
  void noValue() {
    assertNull(Functions.noValue(null));
    assertNull(Functions.noValue("Test"));
    assertNull(Functions.noValue(true));
    assertNull(Functions.noValue(42));
  }
}