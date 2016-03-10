package de.heinerion.betriebe.tools;

public final class MathUtil {
  private MathUtil() {
  }

  public static int product(int... values) {
    int result = 1;
    for (int i : values) {
      result *= i;
    }
    return result;
  }
}
