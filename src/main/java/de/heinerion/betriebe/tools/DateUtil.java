package de.heinerion.betriebe.tools;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateUtil {
  private static final DateTimeFormatter WRITE_FORMATTER = DateTimeFormatter
      .ofPattern("dd.MM.yyyy");

  private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter
      .ofPattern("d.M.yyyy");

  private static final Pattern DATE_PATTERN = Pattern
      .compile("([0-3]?[0-9])\\.([012]?[0-9])\\.([0-9]{4})");

  private DateUtil() {
  }

  private static LocalDate convertDates(Date date) {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
  }

  public static String extractDateString(String wholeString) {
    Matcher matcher = DATE_PATTERN.matcher(wholeString);
    String result = null;

    if (matcher.find()) {
      result = matcher.group();
    }

    return result;
  }

  public static String format(Date date) {
    return format(convertDates(date));
  }

  public static String format(LocalDate date) {
    return date.format(WRITE_FORMATTER);
  }

  public static LocalDate parse(String source) {
    return LocalDate.parse(source, PARSE_FORMATTER);
  }
}
