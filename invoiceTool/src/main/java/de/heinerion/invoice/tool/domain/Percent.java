package de.heinerion.invoice.tool.domain;

import java.util.Objects;

public class Percent {

  private final int percentage;

  public Percent(int percentage) {
    this.percentage = percentage;
  }

  public double asFactor() {
    return percentage / 100d;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Percent percent = (Percent) o;
    return percentage == percent.percentage;
  }

  @Override
  public int hashCode() {
    return Objects.hash(percentage);
  }

  @Override
  public String toString() {
    return percentage + "%";
  }
}
