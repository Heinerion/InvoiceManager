package de.heinerion.betriebe.loading;

class LoadingException extends RuntimeException {
  <T> LoadingException(Class<T> message) {
    super("no loader registered for class " + message.getCanonicalName());
  }
}
