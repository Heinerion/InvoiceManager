package de.heinerion.money;

import de.heinerion.betriebe.tools.ParsingTools;

import java.text.DecimalFormat;

public abstract class AbstractMoney implements Money, Comparable<Money> {
  private static DecimalFormat df = new DecimalFormat(",##0.00");

  private double value;
  private String currency;

  protected AbstractMoney(double aValue, String aCurrency) {
    this.value = aValue;
    this.currency = aCurrency;
  }

  @Override
  public final int compareTo(Money o) {
    return toString().compareTo(o.toString());
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof Money && compareTo((Money) obj) == 0;
  }

  @Override
  public final String getCurrency() {
    return currency;
  }

  @Override
  public final double getValue() {
    return value;
  }

  protected static final double parseValue(String input) {
    return ParsingTools.parseDouble(input);
  }

  @Override
  public final String toString() {
    return df.format(value) + " " + currency;
  }
}
