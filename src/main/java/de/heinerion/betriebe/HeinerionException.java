package de.heinerion.betriebe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

class HeinerionException extends RuntimeException {
  private HeinerionException(Throwable t) {
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
