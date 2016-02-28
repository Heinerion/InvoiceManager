package de.heinerion.betriebe.tools.strings;

import de.heinerion.betriebe.data.Constants;

public final class Strings {
  private Strings() {
  }

  /**
   * Prüft, ob String leer oder nicht vorhanden (null) ist.<br>
   * Verwendet <code>String.isEmpty</code>
   *
   * @param string
   *          zu überprüfende Zeichenkette
   * @return <code>true</code> wenn
   *         <ul>
   *         <li>"" (length == 0)
   *         <li>" " (beliebig viele Leerzeichen)
   *         <li><code>null</code>
   *         </ul>
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
   * @param in
   *          Der Umzuwandelnde Eingabestring
   * @return Der geänderte Ausgabestring
   */
  public static String nToSlash(String in) {
    String out = "";
    for (final String string : in.split(Constants.NEWLINE)) {
      if ("".equals(string.trim())) {
        out += string + Constants.NEWLINE;
      } else {
        out += string + "\\\\" + Constants.NEWLINE;
      }
    }
    return out;
  }
}
