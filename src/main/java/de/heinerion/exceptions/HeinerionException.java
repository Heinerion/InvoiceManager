package de.heinerion.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HeinerionException extends RuntimeException {
  public HeinerionException() {
    super();
  }

  public HeinerionException(String message) {
    super(message);
  }

  public HeinerionException(String message, Throwable t) {
    super(message, t);
  }

  public HeinerionException(Throwable t) {
    super(t);
  }

  public static void rethrow(Throwable t) {
    throw new HeinerionException(t);
  }

  public static void handleException(Class<?> originalClass, Throwable e) {
    Logger logger = LogManager.getLogger(originalClass);
    if (logger.isErrorEnabled()) {
      logger.error(e);
    }
    rethrow(e);
  }
}
