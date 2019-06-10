package de.heinerion.invoice.tool.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EuroTest {

  @Test
  public void add() {
    assertEquals(new Euro(1, 50), new Euro(1, 0).add(new Euro(0, 50)));
    assertEquals(new Euro(1, 0), new Euro(0, 50).add(new Euro(0, 50)));
    assertEquals(new Euro(101, 15), new Euro(70, 65).add(new Euro(30, 50)));
  }

  @Test
  public void asCents() {
    assertEquals(120486, new Euro(1204, 86).asCents());
    assertEquals(-1750, new Euro(-17, 50).asCents());
    assertEquals(105, new Euro(1, 5).asCents());
  }

  @Test
  public void fromCent() {
    assertEquals(new Euro(12, 53), Euro.fromCents(1253));
    assertEquals(new Euro(-64387, 35), Euro.fromCents(-6438735));
  }
}