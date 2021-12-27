package de.heinerion.invoice.view.swing.home.receiver.money;

import de.heinerion.invoice.ParsingUtil;

import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Locale;

public class Money implements Comparable<Money> {
  /**
   * Precision for double to long conversion.
   * <p>
   * The number of zeros is equal to the number of decimal places to convert
   */
  private static final int PRECISION = 100000;

  private static final DecimalFormat df = new DecimalFormat(",##0.00");

  private final long value;
  private final Currency currency = Currency.getInstance(Locale.GERMANY);

  private Money(double aValue) {
    this.value = (long) aValue * PRECISION;
  }

  public static Money parse(String text) {
    return of(ParsingUtil.parseDouble(text));
  }

  public static Money of(double theValue) {
    return new Money(theValue);
  }

  public Money add(Money money) {
    if (money != null) {
      return of(getValue() + money.getValue());
    }
    // else could look for conversion rate or such
    throw new OperationNotYetImplementedException();
  }

  public Money divideBy(double parts) {
    return of(getValue() / parts);
  }

  public Money sub(Money money) {
    if (money != null) {
      return of(getValue() - money.getValue());
    }
    // else could look for conversion rate or such
    throw new OperationNotYetImplementedException();
  }

  public Money times(double times) {
    return of(getValue() * times);
  }

  /**
   * Compares this instance to the given Money instance by String representation ({@link #toString})
   *
   * @param other
   *     the other Money instance
   *
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
    return currency.hashCode() + Long.hashCode(value);
  }

  public final double getValue() {
    return (double) value / PRECISION;
  }

  public String getValueFormatted() {
    return df.format(getValue()) + " " + currency.getSymbol();
  }

  @Override
  public final String toString() {
    return df.format(getValue()) + " " + currency.getSymbol() + " [" + value + "]";
  }

  private static class OperationNotYetImplementedException extends RuntimeException {
  }
}
