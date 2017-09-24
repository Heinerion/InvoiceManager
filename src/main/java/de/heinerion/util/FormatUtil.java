package de.heinerion.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public final class FormatUtil {
  /* TODO Den ganzen Lokalisierungskäse nochmal überdenken */

  private static final String DECIMAL_PATTERN = ",##0.00";
  private static final String PERCENTAGE_PATTERN = "##0%";

  private static DecimalFormat dfAmerican;
  private static DecimalFormat dfLocale;
  private static DecimalFormat dfPercentage = new DecimalFormat(
      PERCENTAGE_PATTERN);

  static {
    final Locale locale = new Locale("en", "US");

    dfAmerican = (DecimalFormat) NumberFormat.getNumberInstance(locale);
    dfAmerican.applyPattern(DECIMAL_PATTERN);

    dfLocale =   (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);
    dfLocale.applyPattern(DECIMAL_PATTERN);
  }

  private FormatUtil() {
  }

  public static String formatAmericanDecimal(double value) {
    return dfAmerican.format(value);
  }

  public static String formatLocaleDecimal(double value) {
    return dfLocale.format(value);
  }

  public static String formatPercentage(double value) {
    return dfPercentage.format(value);
  }
}
