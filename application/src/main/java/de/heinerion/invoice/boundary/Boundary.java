package de.heinerion.invoice.boundary;

import de.heinerion.invoice.Translator;

class Boundary {
  private Boundary() {
  }

  /**
   * delivers the translation of the given key from this packages properties
   *
   * @param key       the translation key as found in {@code Boundary.properties}
   * @param arguments (Optional) Arguments to be substituted in the properties value
   *
   * @return the translation of the given key or, if no translation is found, the key in single quotes
   *
   * @see Translator#translate(Class, String, Object...)
   */
  public static String translate(String key, Object... arguments) {
    return Translator.translate(Boundary.class, key, arguments);
  }
}
