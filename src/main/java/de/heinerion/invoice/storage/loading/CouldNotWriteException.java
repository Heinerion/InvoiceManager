package de.heinerion.invoice.storage.loading;

class CouldNotWriteException extends RuntimeException {
  CouldNotWriteException(Throwable t) {
    super(t);
  }
}
