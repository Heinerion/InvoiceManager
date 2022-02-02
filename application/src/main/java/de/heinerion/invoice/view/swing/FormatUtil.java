package de.heinerion.invoice.view.swing;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public final class FormatUtil {
  private static final String DECIMAL_PATTERN = ",##0.00";

  private static final DecimalFormat dfLocale;

  static {
    dfLocale = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);
    dfLocale.applyPattern(DECIMAL_PATTERN);
  }

  private FormatUtil() {
  }

  public static String formatLocaleDecimal(double value) {
    return dfLocale.format(value);
  }
}
