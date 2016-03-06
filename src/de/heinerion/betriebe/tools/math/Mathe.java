package de.heinerion.betriebe.tools.math;

public final class Mathe {
  private Mathe() {
  }

  public static int produkt(int[] werte) {
    int produkt = 1;
    for (int i : werte) {
      produkt *= i;
    }
    return produkt;
  }
}
