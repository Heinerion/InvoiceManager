package de.heinerion.betriebe.tools;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import de.heinerion.betriebe.tools.strings.Strings;

public final class ParsingTools {
  private ParsingTools() {
  }

  public static double parseDouble(String input) {
    double result = 0;

    try {
      result = perseByLocale(input, Locale.US);
    } catch (ParseException ex) {
      try {
        result = perseByLocale(input, Locale.GERMANY);
      } catch (ParseException exe) {
        throw new RuntimeException(exe);
      }
    }

    return result;
  }

  /**
   * @param input
   * @return
   * @throws ParseException
   */
  private static double perseByLocale(String input, Locale locale)
      throws ParseException {
    double result;
    if (input == null || Strings.isEmpty(input)) {
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
}
