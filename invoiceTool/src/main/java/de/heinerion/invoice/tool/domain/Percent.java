package de.heinerion.invoice.tool.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Percent implements Comparable<Percent> {

  private final BigDecimal percentage;

  public Percent(int percentage) {
    this(new BigDecimal(percentage));
  }

  public Percent(BigDecimal percentage) {
    this.percentage = percentage;
  }

  public BigDecimal asFactor() {
    return percentage.divide(BigDecimal.valueOf(100L), 4, BigDecimal.ROUND_HALF_UP);
  }

  public BigDecimal getPercentage() {
    return percentage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return compareTo((Percent) o) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(percentage);
  }

  @Override
  public String toString() {
    return percentage + "%";
  }

  @Override
  public int compareTo(Percent o) {
    return percentage.compareTo(o.percentage);
  }
}
