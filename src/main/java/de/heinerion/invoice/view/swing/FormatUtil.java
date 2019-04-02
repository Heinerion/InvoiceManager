package de.heinerion.invoice.view.swing;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public final class FormatUtil {
  private static final String DECIMAL_PATTERN = ",##0.00";
  private static final String PERCENTAGE_PATTERN = "##0%";

  private static DecimalFormat dfLocale;
  private static DecimalFormat dfPercentage = new DecimalFormat(PERCENTAGE_PATTERN);

  static {
    dfLocale = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);
    dfLocale.applyPattern(DECIMAL_PATTERN);
  }

  private FormatUtil() {
  }

  public static String formatLocaleDecimal(double value) {
    return dfLocale.format(value);
  }

  public static String formatPercentage(double value) {
    return dfPercentage.format(value);
  }
}
