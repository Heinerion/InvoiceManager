package de.heinerion.invoice;

import lombok.extern.flogger.Flogger;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.logging.Level;

@Flogger
public final class NumberParser {

  private NumberParser() {
  }

  public static OptionalDouble parseDouble(String input) {
    String cleaned = input.strip().replace(" ", "");

    if (cleaned.isBlank() || !cleaned.matches("[\\d.,]*\\d")) {
      return OptionalDouble.empty();
    }

    try {
      return OptionalDouble.of(parseByLocale(
          cleaned,
          cleaned.lastIndexOf(',') > cleaned.lastIndexOf('.')
              ? Locale.GERMANY
              : Locale.US));
    } catch (ParseException exe) {
      log.at(Level.WARNING).withCause(exe).log();
      throw new ParsingException(exe);
    }
  }

  private static double parseByLocale(String input, Locale locale)
      throws ParseException {
    return Optional
        .of(NumberFormat.getInstance(locale).parse(input))
        .map(Number::doubleValue)
        .orElse(0d);
  }

  static class ParsingException extends RuntimeException {
    ParsingException(Throwable cause) {
      super(cause);
    }
  }
}
