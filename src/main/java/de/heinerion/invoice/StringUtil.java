package de.heinerion.invoice;

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
}
