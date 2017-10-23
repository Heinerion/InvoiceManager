package de.heinerion.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class Translator {
  private Translator() {
  }

  public static String translate(String key) {
    return translate(determineResourceBundle(key), key);
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
      case "menu":
        resourceName = prefix;
        break;
      default:
        resourceName = "base";
    }

    return "translation." + resourceName;
  }

  private static String translate(ResourceBundle resourceBundle, String key) {
    return resourceBundle.containsKey(key) ? resourceBundle.getString(key) : decorateUnknownKey(key);
  }

  private static String decorateUnknownKey(String key) {
    return "'" + key + "'";
  }
}
