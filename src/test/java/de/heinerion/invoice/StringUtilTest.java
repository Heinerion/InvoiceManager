package de.heinerion.invoice;

import org.junit.Assert;
import org.junit.Test;

public final class StringUtilTest {

  @Test
  public void testIsEmptyString() {
    Assert.assertTrue(StringUtil.isEmpty(null));
    Assert.assertTrue(StringUtil.isEmpty(""));
    Assert.assertTrue(StringUtil.isEmpty(" "));
    Assert.assertTrue(StringUtil.isEmpty("\t"));
    Assert.assertTrue(StringUtil.isEmpty("\n"));
    Assert.assertTrue(StringUtil.isEmpty("\t\n\t\n"));
  }

  @Test
  public void testIsNotEmptyString() {
    Assert.assertFalse(StringUtil.isEmpty("Test"));
    Assert.assertFalse(StringUtil.isEmpty("" + 0));
  }
}
