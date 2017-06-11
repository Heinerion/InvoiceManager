package de.heinerion.betriebe.tools;

import de.heinerion.betriebe.data.Constants;

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
    for (String string : in.split(Constants.NEWLINE)) {
      if ("".equals(string.trim())) {
        out.append(string);
      } else {
        out.append(string)
            .append("\\\\");
      }
      out.append(Constants.NEWLINE);
    }
    return out.toString();
  }
}
