package de.heinerion.betriebe.tools.strings;

import org.junit.Assert;
import org.junit.Test;

public final class StringsTest {
  public StringsTest() {

  }

  @Test
  public void testIsEmptyString() {
    Assert.assertTrue(Strings.isEmpty(null));
    Assert.assertTrue(Strings.isEmpty(""));
    Assert.assertTrue(Strings.isEmpty(" "));
    Assert.assertTrue(Strings.isEmpty("\t"));
    Assert.assertTrue(Strings.isEmpty("\n"));
    Assert.assertTrue(Strings.isEmpty("\t\n\t\n"));
  }

  @Test
  public void testIsNotEmptyString() {
    Assert.assertFalse(Strings.isEmpty("Test"));
    Assert.assertFalse(Strings.isEmpty("" + 0));
  }
}
