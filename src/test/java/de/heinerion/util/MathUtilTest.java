package de.heinerion.util;

import org.assertj.core.api.SoftAssertions;
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
    assertEquals(6, MathUtil.product(1, 2, 3));
  }

  @Test
  public void product() {
    SoftAssertions softly = new SoftAssertions();

    softly.assertThat(MathUtil.product()).as("No Argument").isEqualTo(1);
    softly.assertThat(MathUtil.product(5)).as("Single Argument 5").isEqualTo(5);
    softly.assertThat(MathUtil.product(1, 2, 3)).as("1 * 2 * 3").isEqualTo(6);

    softly.assertAll();
  }
}