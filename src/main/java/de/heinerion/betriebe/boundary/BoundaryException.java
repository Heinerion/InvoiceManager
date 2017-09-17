package de.heinerion.betriebe.boundary;

class BoundaryException extends RuntimeException{
  private BoundaryException(Throwable cause) {
    super(cause);
  }

  static void rethrow(Throwable t) {
    throw new BoundaryException(t);
  }
}
