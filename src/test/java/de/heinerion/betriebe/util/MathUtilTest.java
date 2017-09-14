package de.heinerion.betriebe.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MathUtilTest {

  @Test
  public void testProductNoArgs() throws Exception {
    assertEquals(1, MathUtil.product());
  }

  @Test
  public void testProductOneArg() throws Exception {
    assertEquals(5, MathUtil.product(5));
  }

  @Test
  public void testProductThreeArgs() throws Exception {
    assertEquals(6, MathUtil.product(1,2,3));
  }
}