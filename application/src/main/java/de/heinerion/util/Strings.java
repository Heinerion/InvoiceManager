package de.heinerion.util;

public class Strings {
  /**
   * Checks if a String is blank. A blank string is one that is either {@code null}, empty, or all characters are {@link
   * Character#isWhitespace(char)}.
   *
   * <p>
   * Copied from Log4j org.apache.logging.log4j.util.Strings#isBlank(java.lang.String)
   * </p>
   *
   * @param s
   *     the String to check, may be {@code null}
   *
   * @return {@code true} if the String is {@code null}, empty, or all characters are {@link
   *     Character#isWhitespace(char)}
   */
  public static boolean isBlank(final String s) {
    if (s == null || s.isEmpty()) {
      return true;
    }
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (!Character.isWhitespace(c)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if a String is not blank. The opposite of {@link #isBlank(String)}.
   *
   * <p>
   * Copied from Log4j org.apache.logging.log4j.util.Strings#isNotBlank(java.lang.String)
   * </p>
   *
   * @param s
   *     the String to check, may be {@code null}
   *
   * @return {@code true} if the String is non-{@code null} and has content after being trimmed.
   */
  public static boolean isNotBlank(final String s) {
    return !isBlank(s);
  }
}
