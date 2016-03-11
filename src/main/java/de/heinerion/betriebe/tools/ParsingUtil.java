package de.heinerion.betriebe.tools;

import de.heinerion.betriebe.exceptions.HeinerionException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public final class ParsingUtil {
  private ParsingUtil() {
  }

  public static double parseDouble(String input) {
    double result = 0;

    try {
      result = perseByLocale(input, Locale.US);
    } catch (ParseException ex) {
      try {
        result = perseByLocale(input, Locale.GERMANY);
      } catch (ParseException exe) {
        HeinerionException.handleException(ParsingUtil.class, exe);
      }
    }

    return result;
  }

  private static double perseByLocale(String input, Locale locale)
      throws ParseException {
    double result;
    if (input == null || StringUtil.isEmpty(input)) {
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
