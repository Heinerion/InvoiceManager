package de.heinerion.invoice.tool.domain;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PercentTest {

  @Test
  public void compareTo() {
    Percent one = new Percent(1);
    Percent ten = new Percent(10);
    Percent dec = new Percent(BigDecimal.valueOf(10.7d));

    assertTrue(one.compareTo(ten) < 0);
    assertTrue(one.compareTo(dec) < 0);
    assertTrue(ten.compareTo(one) > 0);
    assertTrue(ten.compareTo(dec) < 0);
    assertTrue(dec.compareTo(one) > 0);
    assertTrue(dec.compareTo(ten) > 0);

    assertEquals(0, one.compareTo(new Percent(1)));
    assertEquals(0, ten.compareTo(new Percent(10)));
    assertEquals(0, dec.compareTo(new Percent(BigDecimal.valueOf(10.70000000000))));

    assertEquals(one, new Percent(1));
    assertEquals(ten, new Percent(10));
    assertEquals(dec, new Percent(new BigDecimal(BigInteger.valueOf(107),1)));
  }
}