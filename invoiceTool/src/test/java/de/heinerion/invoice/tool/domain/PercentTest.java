package de.heinerion.invoice.tool.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PercentTest {

  @Test
  public void compareTo() {
    Percent one = new Percent(1);
    Percent ten = new Percent(10);

    assertTrue(one.compareTo(ten) < 0);
    assertTrue(ten.compareTo(one) > 0);

    assertEquals(0, one.compareTo(new Percent(1)));
    assertEquals(0, ten.compareTo(new Percent(10)));

    assertEquals(one, new Percent(1));
    assertEquals(ten, new Percent(10));
  }
}