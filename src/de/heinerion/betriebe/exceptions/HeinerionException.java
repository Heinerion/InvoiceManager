package de.heinerion.betriebe.exceptions;

public class HeinerionException extends RuntimeException {
  public HeinerionException() {
    super();
  }

  public HeinerionException(Throwable t) {
    super(t);
  }

  public static void rethrow(Throwable t) {
    throw new HeinerionException(t);
  }
}
