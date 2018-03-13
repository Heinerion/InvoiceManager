package de.heinerion.invoice.view.swing.home.receiver.money;

import de.heinerion.invoice.ParsingUtil;

import java.text.DecimalFormat;

public abstract class AbstractMoney implements Money, Comparable<Money> {
  private static final DecimalFormat df = new DecimalFormat(",##0.00");

  private final double value;
  private final String currency;

  AbstractMoney(double aValue, String aCurrency) {
    this.value = aValue;
    this.currency = aCurrency;
  }

  static double parseValue(String input) {
    return ParsingUtil.parseDouble(input);
  }

  /**
   * Compares this instance to the given Money instance by String representation ({@link #toString})
   *
   * @param other the other Money instance
   * @return a value greater 0 if this is bigger than {@code other}; less than 0 otherwise; 0 indicates equality
   */
  @Override
  public final int compareTo(Money other) {
    if (other == null) {
      return 1; // bigger than null
    } else if (equals(other)) {
      return 0; // as big as itself
    } else {
      return toString().compareTo(other.toString());
    }
  }

  @Override
  public final boolean equals(Object obj) {
    return obj != null && (obj == this || obj instanceof Money && hashCode() == obj.hashCode());
  }

  @Override
  public int hashCode() {
    // the hash uses the int representation, to create some tolerance for the double values
    return currency.hashCode() + Integer.hashCode((int) value * 10000);
  }

  @Override
  public final String getCurrency() {
    return currency;
  }

  @Override
  public final double getValue() {
    return value;
  }

  @Override
  public final String toString() {
    return df.format(value) + " " + currency;
  }
}
