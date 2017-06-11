package de.heinerion.betriebe.tools;

import de.heinerion.betriebe.data.Constants;
import de.heinerion.betriebe.fileoperations.Syntax;

public final class StringUtil {
  private StringUtil() {
  }

  /**
   * Prüft, ob String leer oder nicht vorhanden (null) ist.<br>
   * Verwendet <code>String.isEmpty</code>
   *
   * @param string zu überprüfende Zeichenkette
   * @return <code>true</code> wenn
   * <ul>
   * <li>"" (length == 0)
   * <li>" " (beliebig viele Leerzeichen)
   * <li><code>null</code>
   * </ul>
   * @see java.lang.String#isEmpty()
   */
  public static boolean isEmpty(String string) {
    return (string == null) || string.trim().isEmpty();
  }

  /**
   * Wandelt nicht druckbare Zeilenumbrüche in Latex Zeilenumbüche<br>
   * "<code>\n</code>" wird zu "<code> \\\\ </code>" (Im Quelltext "
   * <code>\\</code>" )
   *
   * @param in Der Umzuwandelnde Eingabestring
   * @return Der geänderte Ausgabestring
   */
  public static String nToSlash(String in) {
    StringBuilder out = new StringBuilder();

    for (String string : getNewlineSeparatedParts(in)) {
      appendContent(out, string);
    }

    return out.toString();
  }

  private static String[] getNewlineSeparatedParts(String in) {
    if (!isEmpty(in)) {
      return in.split(Constants.NEWLINE);
    }

    return new String[]{};
  }

  private static void appendContent(StringBuilder out, String string) {
    appendText(out, string);

    if (!isEmpty(string)) {
      // Syntax.BR equals the Latex newline
      appendText(out, Syntax.BR);
    }

    appendText(out, Constants.NEWLINE);
  }

  private static void appendText(StringBuilder out, String string) {
    out.append(string);
  }
}
