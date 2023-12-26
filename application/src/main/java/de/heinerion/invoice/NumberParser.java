package de.heinerion.invoice;

import de.heinerion.contract.Contract;
import lombok.extern.flogger.Flogger;

import java.text.*;
import java.util.*;
import java.util.logging.Level;

@Flogger
public final class NumberParser {

  private NumberParser() {
  }

  public static OptionalDouble parseDouble(String input) {
    Contract.requireNotNull(input, "input");
    String cleaned = input.strip().replace(" ", "");

    return cleaned.isBlank() || !cleaned.matches("[\\d.,]*\\d")
        ? OptionalDouble.empty()
        : OptionalDouble.of( safelyParseDouble(cleaned));
  }

  private static double safelyParseDouble(String cleaned) {
    try {
      return parseGermanOrUsDouble(cleaned);
    } catch (ParseException exe) {
      log.at(Level.WARNING).withCause(exe).log();
      throw new ParsingException(exe);
    }
  }

  private static double parseGermanOrUsDouble(String doubleLiteral) throws ParseException {
    Locale locale = determineLocale(doubleLiteral);
    return parseByLocale(doubleLiteral, locale);
  }

  private static Locale determineLocale(String doubleLiteral) {
    return doubleLiteral.lastIndexOf(',') > doubleLiteral.lastIndexOf('.')
        ? Locale.GERMANY
        : Locale.US;
  }

  private static double parseByLocale(String input, Locale locale) throws ParseException {
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
