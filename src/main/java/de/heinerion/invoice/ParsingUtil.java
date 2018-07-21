package de.heinerion.invoice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public final class ParsingUtil {
  private static Logger logger = LogManager.getLogger(ParsingUtil.class);

  private ParsingUtil() {
  }

  public static double parseDouble(String input) {
    double result = 0;

    try {
      result = parseByLocale(input, Locale.US);
    } catch (ParseException ex) {
      try {
        result = parseByLocale(input, Locale.GERMANY);
      } catch (ParseException exe) {
        if (logger.isErrorEnabled()) {
          logger.error(exe);
        }
        throw new ParsingException(exe);
      }
    }

    return result;
  }

  private static double parseByLocale(String input, Locale locale)
      throws ParseException {
    double result;
    if (input == null || input.trim().isEmpty()) {
      result = 0;
    } else {
      final NumberFormat format = NumberFormat.getInstance(locale);
      final Number number = format.parse(input);
      if (number == null) {
        result = 0;
      } else {
        result = number.doubleValue();
      }
    }
    return result;
  }

  static class ParsingException extends RuntimeException {
    ParsingException(Throwable cause) {
      super(cause);
    }
  }
}
