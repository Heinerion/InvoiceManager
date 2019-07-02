package de.heinerion.invoice.tool.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents an amount of Euro
 */
public class Euro {
  public static final Euro ZERO = new Euro(0, 0);

  private final int euro;
  private final int cent;

  private Euro(int euro, int cent) {
    this.euro = euro;
    this.cent = Math.abs(cent);
  }

  public static Euro of(int euro) {
    return new Euro(euro, 0);
  }

  public static Euro of(int euro, int cent) {
    return new Euro(euro, cent);
  }

  public static Euro fromCents(int cents) {
    return new Euro(cents / 100, cents % 100);
  }

  public Euro add(Euro b) {
    int cent = this.cent + b.cent;
    return new Euro(this.euro + b.euro + cent / 100, cent % 100);
  }

  public Euro multiply(Percent percentage) {
    Objects.requireNonNull(percentage);
    return fromCents((percentage.asFactor().multiply(new BigDecimal(asCents()))).intValue());
  }

  public Euro multiply(int times) {
    return fromCents(asCents() * times);
  }

  public int asCents() {
    int factor = 1;
    if (euro < 0) {
      factor = -1;
    }
    return euro * 100 + factor * cent;
  }

  public int getEuros() {
    return euro;
  }

  public int getCents() {
    return cent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return hashCode() == o.hashCode();
  }

  @Override
  public int hashCode() {
    return asCents();
  }

  @Override
  public String toString() {
    return String.format("%d,%02d €", euro, cent);
  }
}
