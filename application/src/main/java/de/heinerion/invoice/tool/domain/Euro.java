package de.heinerion.invoice.tool.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents an amount of Euro
 */
public class Euro {
  public static final Euro ZERO = new Euro(0, 0);

  private final int euros;
  private final int cents;

  private Euro(int euros, int cents) {
    this.euros = euros;
    this.cents = Math.abs(cents);
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
    int newCents = this.cents + b.cents;
    return new Euro(this.euros + b.euros + newCents / 100, newCents % 100);
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
    if (euros < 0) {
      factor = -1;
    }
    return euros * 100 + factor * cents;
  }

  public int getEuros() {
    return euros;
  }

  public int getCents() {
    return cents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return hashCode() == o.hashCode();
  }

  @Override
  public int hashCode() {
    return asCents();
  }

  @Override
  public String toString() {
    return String.format("%d,%02d €", euros, cents);
  }
}
