package de.heinerion.money;

import org.junit.Assert;
import org.junit.Test;

public final class EuroTest {
  public EuroTest() {
  }

  @Test
  public void testAdd() {
    final Euro a = new Euro(2.5);
    final Euro b = new Euro(1.25);
    final Euro c = a.add(b);
    final Euro expected = new Euro(3.75);

    Assert.assertEquals(expected, c);
  }

  @Test
  public void testDivideBy() {
    final Euro a = new Euro(2.75);
    final double b = 2d;
    final Euro c = a.divideBy(b);
    final Euro expected = new Euro(1.38);

    Assert.assertEquals(expected, c);
  }
}
