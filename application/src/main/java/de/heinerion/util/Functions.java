package de.heinerion.util;

public class Functions {
  public static <A, B> void doNothing(A a, B b) {
  }

  public static <T> boolean alwaysTrue(T ignored) {
    return true;
  }

  public static <T> boolean alwaysFalse(T ignored) {
    return false;
  }
}
