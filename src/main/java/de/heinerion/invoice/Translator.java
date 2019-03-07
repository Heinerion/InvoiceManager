package de.heinerion.invoice;

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
   * The key {@code error.dateFormat} will, for example, be read from the {@code error.properties} Resource Bundle.
   * </p>
   *
   * @param key       will be used to determine the Resource Bundle and to retrieve its value from that
   * @param arguments (Optional) Arguments to be substituted in the properties value
   *
   * @return the value to the given key, if the Resource Bundle could be determined and a value for the given key is defined;<p>
   * will return the key in single quotes otherwise (e.g. {@code 'unknown.key'})
   * </p>
   */
  public static String translate(String key, Object... arguments) {
    Objects.requireNonNull(key, "key must not be null");

    return translate(determineResourceBundle(key), key, arguments);
  }

  private static ResourceBundle determineResourceBundle(String key) {
    return getResourceBundle(getPrefix(key));
  }

  private static String getPrefix(String key) {
    return key.contains(".") ? key.split("\\.", 2)[0] : "";
  }

  private static ResourceBundle getResourceBundle(String prefix) {
    return ResourceBundle.getBundle(determineBaseName(prefix), Locale.getDefault());
  }

  private static String determineBaseName(String prefix) {
    String resourceName;

    switch (prefix) {
      case "error":
      case "controls":
        resourceName = prefix;
        break;
      default:
        resourceName = "base";
    }

    return "translation." + resourceName;
  }

  /**
   * Delivers the value of the given key from a Resource Bundle derived by the given class.
   * <p>
   * Returns the key enclosed in single quotes if there was no value found for it.
   * </p>
   *
   * @param moduleClass is used to derive the Resource Bundle to be read from
   * @param key         will be looked for in the Resource Bundle
   * @param arguments   (Optional) Arguments to be substituted in the properties value
   *
   * @return the translation of the given key or, if no translation is found, the key in single quotes
   */
  public static String translate(Class moduleClass, String key, Object... arguments) {
    Objects.requireNonNull(moduleClass, "moduleClass must not be null");
    Objects.requireNonNull(key, "key must not be null");

    return translate(ResourceBundle.getBundle(moduleClass.getCanonicalName()), key, arguments);
  }

  private static String translate(ResourceBundle resourceBundle, String key, Object... arguments) {
    return MessageFormat.format(readProperty(resourceBundle, key), arguments);
  }

  private static String readProperty(ResourceBundle resourceBundle, String key) {
    return resourceBundle.containsKey(key) ? resourceBundle.getString(key) : decorateUnknownKey(key);
  }

  private static String decorateUnknownKey(String key) {
    return "'" + key + "'";
  }
}
