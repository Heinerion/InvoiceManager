package de.heinerion.invoice.tool.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EuroTest {

  @Test
  public void add() {
    assertEquals(Euro.of(1, 50), Euro.of(1, 0).add(Euro.of(0, 50)));
    assertEquals(Euro.of(1, 0), Euro.of(0, 50).add(Euro.of(0, 50)));
    assertEquals(Euro.of(101, 15), Euro.of(70, 65).add(Euro.of(30, 50)));
  }

  @Test
  public void asCents() {
    assertEquals(120486, Euro.of(1204, 86).asCents());
    assertEquals(-1750, Euro.of(-17, 50).asCents());
    assertEquals(105, Euro.of(1, 5).asCents());
  }

  @Test
  public void fromCent() {
    assertEquals(Euro.of(12, 53), Euro.fromCents(1253));
    assertEquals(Euro.of(-64387, 35), Euro.fromCents(-6438735));
  }
}