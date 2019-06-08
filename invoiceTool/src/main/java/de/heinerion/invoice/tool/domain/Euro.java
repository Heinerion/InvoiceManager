package de.heinerion.invoice.tool.domain;

/**
 * Represents an amount of Euro
 */
public class Euro {
  public static Euro ZERO = new Euro(0, 0);

  private long euro;
  private int cent;

  public Euro(long euro, int cent) {
    this.euro = euro;
    this.cent = cent;
  }

  public Euro add(Euro b) {
    int cent = this.cent + b.cent;
    return new Euro(this.euro + b.euro + cent / 100, cent % 100);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    return hashCode() == o.hashCode();
  }

  @Override
  public int hashCode() {
    return (int) (euro * 100 + cent);
  }

  @Override
  public String toString() {
    return String.format("%d,%02d €", euro, cent);
  }
}
