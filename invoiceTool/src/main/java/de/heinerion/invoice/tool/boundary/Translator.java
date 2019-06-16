package de.heinerion.invoice.tool.boundary;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The Translator reads replacements (or translations) for a given key from a given Resource Bundle
 */
public class Translator {
  private Translator() {
  }

  /**
   * Delivers the value of the given key from a Resource Bundle derived by the key.
   * <p>
   * Returns the key enclosed in single quotes if there was no value found for it.
   * </p>
   * <p>
   * The key {@code error.dateFormat} will, for example, be read from the {@code error.properties}
   * Resource Bundle.
   * </p>
   *
   * @param key       will be used to determine the Resource Bundle and to retrieve its value from
   *                  that
   * @param arguments (Optional) Arguments to be substituted in the properties value
   *
   * @return the value to the given key, if the Resource Bundle could be determined and a value for
   * the given key is defined;<p>
   * will return the key in single quotes otherwise (e.g. {@code 'unknown.key'})
   * </p>
   */
  public static String translate(String key, Object... arguments) {
    Objects.requireNonNull(key, "key must not be null");

    ResourceBundle bundle = ResourceBundle.getBundle("translation.shared", Locale.getDefault());
    return MessageFormat.format(readProperty(bundle, key), arguments);
  }

  private static String readProperty(ResourceBundle resourceBundle, String key) {
    return resourceBundle.containsKey(key) ? resourceBundle.getString(key) : decorateUnknownKey(key);
  }

  private static String decorateUnknownKey(String key) {
    return "''" + key + "''";
  }
}
