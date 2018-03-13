package de.heinerion.invoice.view.swing.home.receiver.money;

import org.junit.Assert;
import org.junit.Test;

public class EuroTest {
  @Test
  public void testAdd() {
    Euro a = Euro.of(2.5);
    Euro b = Euro.of(1.25);
    Euro c = a.add(b);
    Euro expected = Euro.of(3.75);

    Assert.assertEquals(expected, c);
  }

  @Test
  public void testDivideBy() {
    Euro a = Euro.of(2.75);
    double b = 2d;
    Euro c = a.divideBy(b);
    Euro expected = Euro.of(1.38);

    Assert.assertEquals(expected, c);
  }
}
