package de.heinerion.betriebe.services;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Translator {
  private final static ResourceBundle MESSAGES = ResourceBundle.getBundle("translation", Locale.getDefault());

  public static String translate(String key) {
    String result;

    try {
      result = MESSAGES.getString(key);
    } catch (MissingResourceException mre) {
      result = "'" + key + "'";
    }

    return result;
  }
}
