package de.heinerion.betriebe.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateTools {
  private static DateTimeFormatter formatter = DateTimeFormatter
      .ofPattern("dd.MM.yyyy");

  private static DateTimeFormatter parseFormatter = DateTimeFormatter
      .ofPattern("d.M.yyyy");

  private static final Pattern DATE_PATTERN = Pattern
      .compile("([0-3]?[0-9])\\.([012]?[0-9])\\.([0-9]{4})");

  private DateTools() {
  }

  public static LocalDate convertDates(Date date) {
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
    return date.format(formatter);
  }

  public static LocalDate parse(String source) throws DateTimeParseException {
    return LocalDate.parse(source, parseFormatter);
  }

  public static Date parseDate(String wholeString, Locale locale)
      throws ParseException {
    final DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

    return df.parse(extractDateString(wholeString));
  }
}
