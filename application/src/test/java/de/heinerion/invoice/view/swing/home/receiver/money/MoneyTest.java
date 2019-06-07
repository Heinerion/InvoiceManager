package de.heinerion.invoice.view.swing.home.receiver.money;

import org.junit.Assert;
import org.junit.Test;

public class MoneyTest {
  @Test
  public void testAdd() {
    Money a = Money.of(2.5);
    Money b = Money.of(1.25);
    Money c = a.add(b);
    Money expected = Money.of(3.75);

    Assert.assertEquals(expected, c);
  }

  @Test
  public void testDivideBy() {
    Money a = Money.of(2.75);
    double b = 2d;
    Money c = a.divideBy(b);
    Money expected = Money.of(1.38);

    Assert.assertEquals(expected, c);
  }
}
