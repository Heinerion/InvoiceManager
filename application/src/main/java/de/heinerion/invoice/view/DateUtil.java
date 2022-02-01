package de.heinerion.invoice.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
  private static final DateTimeFormatter WRITE_FORMATTER = DateTimeFormatter
      .ofPattern("dd.MM.yyyy");

  private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter
      .ofPattern("d.M.yyyy");

  private DateUtil() {
  }

  public static String format(LocalDate date) {
    return date.format(WRITE_FORMATTER);
  }

  public static LocalDate parse(String source) {
    return LocalDate.parse(source, PARSE_FORMATTER);
  }

}
