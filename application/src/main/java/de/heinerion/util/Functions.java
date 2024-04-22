package de.heinerion.util;

/** Collection of convenience Methods instead of repeated Lambdas */
public class Functions {

  /** {@link java.util.function.BiConsumer} doing nothing */
  public static <A, B> void doNothing(A ignoredA, B ignoredB) {
  }

  /** @return true in every case */
  public static <T> boolean alwaysTrue(T ignored) {
    return true;
  }

  /** @return false in every case */
  public static <T> boolean alwaysFalse(T ignored) {
    return false;
  }
}
