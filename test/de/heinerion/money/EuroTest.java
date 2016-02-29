package de.heinerion.money;

import org.junit.Assert;
import org.junit.Test;

public class EuroTest {
  @Test
  public void testAdd() {
    Euro a = new Euro(2.5);
    Euro b = new Euro(1.25);
    Euro c = a.add(b);
    Euro expected = new Euro(3.75);

    Assert.assertEquals(expected, c);
  }

  @Test
  public void testDivideBy() {
    Euro a = new Euro(2.75);
    double b = 2d;
    Euro c = a.divideBy(b);
    Euro expected = new Euro(1.38);

    Assert.assertEquals(expected, c);
  }
}
