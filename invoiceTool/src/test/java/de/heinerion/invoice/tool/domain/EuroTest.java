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
}