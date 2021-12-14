package de.heinerion.invoice;

import lombok.extern.flogger.Flogger;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Level;

@Flogger
public final class ParsingUtil {
  private ParsingUtil() {
  }

  public static double parseDouble(String input) {
    try {
      return parseByLocale(input, Locale.US);
    } catch (ParseException ex) {
      try {
        return parseByLocale(input, Locale.GERMANY);
      } catch (ParseException exe) {
        log.at(Level.WARNING).withCause(exe).log();
        throw new ParsingException(exe);
      }
    }
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
